package com.example.perpustakaan.perpustakaan.controller;

import com.example.perpustakaan.perpustakaan.entity.Book;
import com.example.perpustakaan.perpustakaan.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            Model model) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Book> bookPage = bookService.findAllBooks(keyword, pageable);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        return "books/index";
    }

    @GetMapping("/create")
    public String createBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/create";
    }

    @PostMapping
    public String saveBook(@ModelAttribute("book") Book book,
            @RequestParam("image") org.springframework.web.multipart.MultipartFile image,
            RedirectAttributes redirectAttributes) {
        if (!image.isEmpty()) {
            try {
                String fileName = saveImage(image);
                book.setCoverImage(fileName);
            } catch (java.io.IOException e) {
                redirectAttributes.addFlashAttribute("error", "Gagal upload gambar!");
                return "redirect:/books/create";
            }
        }
        bookService.addBook(book);
        redirectAttributes.addFlashAttribute("success", "Buku berhasil ditambahkan!");
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        return "books/edit";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") Book book,
            @RequestParam("image") org.springframework.web.multipart.MultipartFile image,
            RedirectAttributes redirectAttributes) {
        // Enforce ID from path variable
        book.setId(id);

        if (!image.isEmpty()) {
            try {
                String fileName = saveImage(image);
                book.setCoverImage(fileName);
            } catch (java.io.IOException e) {
                redirectAttributes.addFlashAttribute("error", "Gagal upload gambar!");
                return "redirect:/books/edit/" + id;
            }
        } else {
            // Keep existing image if no new one uploaded
            Book existingBook = bookService.findBookById(id);
            book.setCoverImage(existingBook.getCoverImage());
        }

        bookService.addBook(book); // save() updates if ID exists
        redirectAttributes.addFlashAttribute("success", "Data buku berhasil diperbarui!");
        return "redirect:/books";
    }

    private String saveImage(org.springframework.web.multipart.MultipartFile image) throws java.io.IOException {
        String uploadDir = "uploads";
        java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

        if (!java.nio.file.Files.exists(uploadPath)) {
            java.nio.file.Files.createDirectories(uploadPath);
        }

        String originalFileName = org.springframework.util.StringUtils.cleanPath(image.getOriginalFilename());
        String fileName = java.util.UUID.randomUUID().toString() + "_" + originalFileName;

        try (java.io.InputStream inputStream = image.getInputStream()) {
            java.nio.file.Path filePath = uploadPath.resolve(fileName);
            java.nio.file.Files.copy(inputStream, filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("success", "Buku berhasil dihapus!");
        return "redirect:/books";
    }

    @GetMapping("/catalog")
    public String catalogBooks(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            Model model) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Book> bookPage = bookService.findAllBooks(keyword, pageable);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        return "books/catalog";
    }
}