package com.example.perpustakaan.perpustakaan.service;

import com.example.perpustakaan.perpustakaan.entity.User;
import com.example.perpustakaan.perpustakaan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username sudah terdaftar");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(com.example.perpustakaan.perpustakaan.entity.Role.VISITOR);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // --- Admin CRUD Operations ---

    public org.springframework.data.domain.Page<User> findAllUsers(String keyword,
            org.springframework.data.domain.Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(keyword, keyword,
                    pageable);
        }
        return userRepository.findAll(pageable);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    @Transactional
    public void saveUser(User user) {
        // Cek jika ini update (id tidak null)
        if (user.getId() != null) {
            User existingUser = findUserById(user.getId());
            // Jika password kosong, pakai password lama
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            //
        } else {
            // New user
            // Cek username duplicate hanya jika new, atau jika username berubah (tapi
            // username usually immutable or unique checked)
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new RuntimeException("Username sudah terdaftar");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}