package com.example.perpustakaan.perpustakaan.controller;

import com.example.perpustakaan.perpustakaan.entity.User;
import com.example.perpustakaan.perpustakaan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Form Login
    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    // Form Registrasi
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "users/register"; // templates/users/register.html
    }

    // Proses Register
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("registrationSuccess", true);
            return "redirect:/users/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/register";
        }
    }

    // --- Admin User Management ---

    @GetMapping
    public String listUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            Model model) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<User> userPage = userService.findAllUsers(keyword, pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        return "users/index";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "User berhasil disimpan!");
            return "redirect:/users";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            // If ID exists, generic redirect back to edit might be better, but simple
            // redirect for now
            return "redirect:/users/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            user.setId(id); // Ensure ID is set
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "Data user berhasil diperbarui!");
            return "redirect:/users";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User berhasil dihapus!");
        return "redirect:/users";
    }
}