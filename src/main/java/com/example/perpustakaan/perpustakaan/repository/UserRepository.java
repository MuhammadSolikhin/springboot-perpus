package com.example.perpustakaan.perpustakaan.repository;

import com.example.perpustakaan.perpustakaan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    org.springframework.data.domain.Page<User> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String username, String fullName, org.springframework.data.domain.Pageable pageable);
}