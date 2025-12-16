package com.example.perpustakaan.perpustakaan.service;

import com.example.perpustakaan.perpustakaan.entity.*;
import com.example.perpustakaan.perpustakaan.repository.BookRepository;
import com.example.perpustakaan.perpustakaan.repository.LoanRepository;
import com.example.perpustakaan.perpustakaan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public org.springframework.data.domain.Page<Loan> findAll(String keyword,
            org.springframework.data.domain.Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return loanRepository.findByUser_FullNameContainingIgnoreCaseOrBook_TitleContainingIgnoreCase(keyword,
                    keyword, pageable);
        }
        return loanRepository.findAll(pageable);
    }

    // LOGIKA PEMINJAMAN
    @Transactional
    public String borrowBooks(Long userId, List<Long> bookIds) {
        User user = userRepository.findById(userId).orElseThrow();
        StringBuilder resultMessage = new StringBuilder();
        int successCount = 0;

        for (Long bookId : bookIds) {
            Book book = bookRepository.findById(bookId).orElseThrow();

            // 1. Cek Stok
            if (book.getStock() <= 0) {
                resultMessage.append("Buku '").append(book.getTitle()).append("' stok habis. ");
                continue;
            }

            // 2. Kurangi Stok
            book.setStock(book.getStock() - 1);
            bookRepository.save(book);

            // 3. Buat Data Peminjaman
            Loan loan = new Loan();
            loan.setUser(user);
            loan.setBook(book);
            loan.setLoanDate(LocalDate.now());
            loan.setDueDate(LocalDate.now().plusDays(7)); // Pinjam 7 hari
            loan.setStatus(LoanStatus.DIAJUKAN); // Status awal

            loanRepository.save(loan);
            successCount++;
        }

        if (successCount == 0) {
            return "Gagal: Semua buku yang dipilih stoknya habis!";
        } else if (successCount < bookIds.size()) {
            return "Berhasil meminjam " + successCount + " buku. Beberapa buku stoknya habis.";
        }

        return "Berhasil mengajukan peminjaman untuk semua buku!";
    }

    @Transactional
    public String borrowBooks(String username, List<Long> bookIds) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        return borrowBooks(user.getId(), bookIds);
    }

    // LOGIKA PENGEMBALIAN & DENDA
    @Transactional
    public String returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        Book book = loan.getBook();

        // 1. Set Tanggal Kembali
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.DIKEMBALIKAN);

        // 2. Kembalikan Stok
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        // 3. Hitung Denda (Rp 2.000 per hari)
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
        if (daysLate > 0) {
            long denda = daysLate * 2000;
            loan.setStatus(LoanStatus.TERLAMBAT); // Ubah status jika telat
            loanRepository.save(loan);
            return "Buku dikembalikan. Terlambat " + daysLate + " hari. Denda: Rp " + denda;
        }

        loanRepository.save(loan);
        return "Buku berhasil dikembalikan tepat waktu.";
    }

    public org.springframework.data.domain.Page<Loan> getLoansByUser(Long userId,
            org.springframework.data.domain.Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow();
        return loanRepository.findByUser(user, pageable);
    }
}