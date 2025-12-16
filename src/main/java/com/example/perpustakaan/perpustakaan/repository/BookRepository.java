package com.example.perpustakaan.perpustakaan.repository;

import com.example.perpustakaan.perpustakaan.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    org.springframework.data.domain.Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title,
            String author, org.springframework.data.domain.Pageable pageable);
}