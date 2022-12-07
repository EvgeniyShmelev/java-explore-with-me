package ru.practicum.ewmmain.users.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 256)
    private String name;
    @Column(name = "email", nullable = false, length = 256)
    private String email;
}
