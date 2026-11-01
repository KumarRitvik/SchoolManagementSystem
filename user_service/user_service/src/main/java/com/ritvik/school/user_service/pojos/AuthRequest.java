package com.ritvik.school.user_service.pojos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {

    @NotNull
    public String username;
    @NotNull
    public String password;

}
