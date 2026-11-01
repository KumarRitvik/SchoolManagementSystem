package com.ritvik.school.user_service.dtos;

import com.ritvik.school.user_service.enums.RoleName;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegistrationDto {

    @Column(nullable = false, unique = true)
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*\\(\\)\\-\\+]).{8,20}$")
    //min 8 alphanumeric and 1 special character
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

}