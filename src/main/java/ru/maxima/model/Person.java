package ru.maxima.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.maxima.model.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть не меньше 2 и не больше 50 символов")
    @Column(name = "name")
    private String name;

    @Min(value = 10, message = "Возраст не может быть меньше 10 лет")
    @Column(name = "age")
    private Integer age;

    @Email
    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Book> books;

    @Column(name = "role")
    private Role role;

    @NotEmpty
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotEmpty
    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @OneToOne
    @JoinColumn(name = "created_person")
    private Person createdPerson;

    @OneToOne
    @JoinColumn(name = "removed_person")
    private Person removedPerson;
}
