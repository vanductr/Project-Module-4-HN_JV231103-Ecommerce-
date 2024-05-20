package rikkei.academy.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

//    @Column(name = "username", unique = true, nullable = false)
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

//    @Column(name = "fullname", nullable = false)
    @Column(name = "fullname")
    private String fullName;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roleSet;
}
