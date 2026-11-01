package com.ritvik.school.user_service.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Table(name = "user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    @Column(name = "user_id")
    private int userId;

    private String username;


    private String password;

    private boolean credentialsNonExpired = true;
    private boolean accountNonExpired = true;

    private boolean enabled = true;
    private boolean accountNonLocked = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;
}