package com.example.perpustakaan.perpustakaan.controller;

import com.example.perpustakaan.perpustakaan.entity.User;
import com.example.perpustakaan.perpustakaan.service.BookService;
import com.example.perpustakaan.perpustakaan.service.LoanService;
import com.example.perpustakaan.perpustakaan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final UserService userService;

    // 1. Tampilkan Daftar Peminjaman
    @GetMapping
    public String listLoans(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            Model model, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<com.example.perpustakaan.perpustakaan.entity.Loan> loanPage;

        if (isAdmin) {
            loanPage = loanService.findAll(keyword, pageable);
        } else {
            User user = userService.findByUsername(authentication.getName());
            loanPage = loanService.getLoansByUser(user.getId(), pageable);
        }

        model.addAttribute("loans", loanPage.getContent());
        model.addAttribute("currentPage", loanPage.getNumber());
        model.addAttribute("totalPages", loanPage.getTotalPages());
        model.addAttribute("totalItems", loanPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        return "loans/list";
    }

    // 2. Tampilkan Form Peminjaman
    @GetMapping("/borrow")
    public String showBorrowForm(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        // Tidak perlu kirim list user lagi, karena yang pinjam adalah user yang login
        return "loans/borrow";
    }

    // 3. Proses Peminjaman
    // 3. Proses Peminjaman
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam java.util.List<Long> bookIds,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        // Gunakan username dari Principal (User yang sedang login)
        String message = loanService.borrowBooks(principal.getName(), bookIds);

        if (message.contains("Gagal")) {
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/loans/borrow";
        } else {
            redirectAttributes.addFlashAttribute("success", message);
            return "redirect:/loans";
        }
    }

    // 4. Proses Pengembalian
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String message = loanService.returnBook(id);
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/loans";
    }
}