package com.example.perpustakaan.perpustakaan.service;

import com.example.perpustakaan.perpustakaan.entity.Book;
import com.example.perpustakaan.perpustakaan.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public org.springframework.data.domain.Page<Book> findAllBooks(String keyword,
            org.springframework.data.domain.Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword,
                    pageable);
        }
        return bookRepository.findAll(pageable);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buku tidak ditemukan"));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public java.util.List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}