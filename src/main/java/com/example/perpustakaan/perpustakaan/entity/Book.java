package com.example.perpustakaan.perpustakaan.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String publisher;

    private int stock;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String synopsis;

    private String coverImage;
}