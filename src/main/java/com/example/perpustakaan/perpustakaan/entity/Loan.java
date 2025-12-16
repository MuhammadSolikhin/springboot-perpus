package com.example.perpustakaan.perpustakaan.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDate loanDate;    
    private LocalDate dueDate;     
    private LocalDate returnDate;  

    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}