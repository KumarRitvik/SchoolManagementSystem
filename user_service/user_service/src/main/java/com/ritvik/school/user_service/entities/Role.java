package com.ritvik.school.user_service.entities;
import com.ritvik.school.user_service.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleName roleName;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();

}