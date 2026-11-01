package com.ritvik.school.user_service.controller;

import com.ritvik.school.user_service.dtos.UserDto;
import com.ritvik.school.user_service.security.JwtUtil;
import com.ritvik.school.user_service.pojos.AuthRequest;
import com.ritvik.school.user_service.dtos.RegistrationDto;
import com.ritvik.school.user_service.entities.User;
import com.ritvik.school.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    // ---------------- REGISTER ----------------
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegistrationDto registrationDto) {
        UserDto userDto= userService.registerUser(registrationDto);
        return new ResponseEntity<>(userDto,HttpStatus.CREATED);
    }

    // ---------------- LOGIN ----------------

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {

        try {
            UsernamePasswordAuthenticationToken upat= new UsernamePasswordAuthenticationToken
                    (authRequest.getUsername(), authRequest.getPassword());
            //AuthRequest(username,password) ko UsernamePasswordAuthenticationToken me wrap
            //kar diya coz AuthenticationManager ko UsernamePasswordAuthenticationToken ka
            //object chahta hai(jisme username/principal aur password/credentials ko
            //Spring Security samajh sakta hai.) "for username/password login" coz
            // AuthRequest is not a standard object with standard fields so use standard
            // field aur standard object me convert kiya jisse SpringSecurity Credentials
            // ko samajh sake...!

            authenticationManager.authenticate(upat);
            //So standard Object with standard fields(like:- jisme username/principal,
            // password/credentials etc) ko AuthenticationManager ke .authenticate() ko
            // provide kar diya jiska kam hai authenticate(who am i?) karna..
            //AuthenticationManager checks:-
            // AuthenticationManager--->AuthenticationProvider-->UserDetailsService-->
            //UserService-->UserRepo-->MySQL
            //MySQLPassword deCrypt nahi hota(from db)
            // to compare with incoming password from frontend.
            //It compares the supplied password against the stored BCrypt password.
            //If correct:
            //Authentication SUCCESS
            // aur aise user authenticate hota hai.


            //Authenticate(who am i?) karane k baad ab ham authenticated User ko token
            //return karenge!...
            User user = userService.getUserByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(user.getUsername(),user.getRole().getRoleName());
            return ResponseEntity.ok(token);
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserDto> getUserDtoByUsername(
            @PathVariable String username) {

        return ResponseEntity.ok(
                userService.getUserDtoByUsername(username)
        );
    }

    // ---------------- TEST ----------------

    @GetMapping("/hello")
    public String hello() {
        return "JWT Authentication Working";
    }

}

