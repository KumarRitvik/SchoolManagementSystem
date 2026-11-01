package com.ritvik.school.user_service.dtos;

import com.ritvik.school.user_service.enums.RoleName;
import lombok.Data;

@Data
public class UserDto {

    private int userId;

    private String username;

    private String password;

    private RoleName roleName;
}
