package com.example.perpustakaan.perpustakaan.config;

import com.example.perpustakaan.perpustakaan.entity.Book;
import com.example.perpustakaan.perpustakaan.entity.Role;
import com.example.perpustakaan.perpustakaan.entity.User;
import com.example.perpustakaan.perpustakaan.repository.BookRepository;
import com.example.perpustakaan.perpustakaan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedBooks();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.setRole(Role.ADMIN);

            User visitor = new User();
            visitor.setUsername("visitor");
            visitor.setPassword(passwordEncoder.encode("visitor123"));
            visitor.setFullName("Visitor User");
            visitor.setRole(Role.VISITOR);

            userRepository.saveAll(List.of(admin, visitor));
            System.out.println("Data Seeder: Users created successfully (admin/admin123, visitor/visitor123)");
        }
    }

    private void seedBooks() {
        if (bookRepository.count() == 0) {
            Book book1 = new Book();
            book1.setTitle("Laskar Pelangi");
            book1.setAuthor("Andrea Hirata");
            book1.setPublisher("Bentang Pustaka");
            book1.setStock(10);
            book1.setSynopsis("Kisah perjuangan anak-anak Laskar Pelangi dalam menempuh pendidikan di Belitong.");
            book1.setCoverImage("https://placeholder.com/150");

            Book book2 = new Book();
            book2.setTitle("Bumi Manusia");
            book2.setAuthor("Pramoedya Ananta Toer");
            book2.setPublisher("Hasta Mitra");
            book2.setStock(5);
            book2.setSynopsis("Roman tetralogi Buru yang menceritakan kehidupan Minke pada masa kolonial Belanda.");
            book2.setCoverImage("https://placeholder.com/150");

            Book book3 = new Book();
            book3.setTitle("Filosofi Kopi");
            book3.setAuthor("Dee Lestari");
            book3.setPublisher("Truedee");
            book3.setStock(7);
            book3.setSynopsis("Kumpulan cerita pendek tentang kopi dan kehidupan.");
            book3.setCoverImage("https://placeholder.com/150");

            bookRepository.saveAll(List.of(book1, book2, book3));
            System.out.println("Data Seeder: Sample books created successfully");
        }
    }
}
