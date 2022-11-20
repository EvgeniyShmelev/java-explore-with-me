package ru.practicum.ewmmain.categories.model;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
}