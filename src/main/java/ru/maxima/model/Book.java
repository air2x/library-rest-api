package ru.maxima.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Назавние не должно быть пустым")
    @Column(name = "name")
    private String name;

    @Column(name = "year_of_production")
    private Integer yearOfProduction;

    @NotEmpty(message = "Автор не должен быть пустым")
    @Column(name = "author")
    private String author;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private Person owner;

    @NotEmpty
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotEmpty
    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @NotEmpty
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "created_person")
    private Person createdPerson;

    @OneToOne
    @JoinColumn(name = "removed_person")
    private Person removedPerson;

    @OneToOne
    @JoinColumn(name = "update_person")
    private Person updatedPerson;
}
