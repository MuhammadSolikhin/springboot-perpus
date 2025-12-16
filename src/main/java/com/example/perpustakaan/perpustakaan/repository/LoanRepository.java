package com.example.perpustakaan.perpustakaan.repository;

import com.example.perpustakaan.perpustakaan.entity.Loan;
import com.example.perpustakaan.perpustakaan.entity.LoanStatus;
import com.example.perpustakaan.perpustakaan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUser(User user);

    List<Loan> findByStatus(LoanStatus status);

    org.springframework.data.domain.Page<Loan> findByUser(User user, org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<Loan> findByUser_FullNameContainingIgnoreCaseOrBook_TitleContainingIgnoreCase(
            String userName, String bookTitle, org.springframework.data.domain.Pageable pageable);

}