package com.ritvik.school.user_service.service;

import com.ritvik.school.user_service.dtos.RegistrationDto;
import com.ritvik.school.user_service.entities.Role;
import com.ritvik.school.user_service.exceptions.ResourceNotFoundException;
import com.ritvik.school.user_service.repositories.RoleRepo;
import com.ritvik.school.user_service.dtos.UserDto;
import com.ritvik.school.user_service.security.AppUserDetails;
import com.ritvik.school.user_service.entities.User;
import com.ritvik.school.user_service.repositories.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setUserId(user.getUserId());
        userDto.setRoleName(user.getRole().getRoleName());
        return userDto;
    }


    //login me user authenticate karne k bad, db se user get karna hai usimg username.. ye wahi hai!..
    public Optional<User> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new AppUserDetails(user);
    }

    //register new user
    public UserDto registerUser(RegistrationDto registrationDto) {

        if (userRepo.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (registrationDto.getRoleName() == null) {
            throw new IllegalArgumentException("Role is required");
        }

        User user = new User();

        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role role = roleRepo.findByRoleName(registrationDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Invalid Role"));
        user.setRole(role);

        user = userRepo.save(user);
        return this.toUserDto(user);


    }

    public UserDto getUserDtoByUsername(String username) {

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(
                "User not found: " + username));

       return  this.toUserDto(user);
    }
}