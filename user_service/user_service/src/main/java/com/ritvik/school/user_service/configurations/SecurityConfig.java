package com.ritvik.school.user_service.configurations;

import com.ritvik.school.user_service.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.csrf(csrf -> csrf.disable())
       // The browser has your session cookie stored in memory, and it automatically attaches that
        //cookie to every request you make to the bank's website.
    //Now imagine you visit a malicious website while still logged into the bank.
               // That malicious website secretly sends:POST /transfer-money amount = ₹50,000
        //The browser may automatically attach your bank's session cookie to that request.
       // So the bank might think:"This request has the user's valid cookie → this must be the user."
                // That's the basic problem CSRF("Cross-Site Request Forgery") protects against.
                //The browser doesn't automatically attach the JWT to HTTP requests(like the session cookie).
                //Your JavaScript must explicitly retrieve the JWT (stored in browser memory or in
                // local memory)and attach it to the Authorization header.

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(auth -> auth//means ye jo default
                                // Authorization object jo spring se mil raha hai
                                .requestMatchers("/auth/register", "/auth/login").permitAll()
                                //matlab register & login ke liye authentication required nahi hai, sab
                                //permitted hsi in 2 url k liye
                        .anyRequest().authenticated()).addFilterBefore(jwtFilter,
                        //matlab any otherRequest(auth/register, auth/login ke alawa)
                        // ke liye authentication required hai,aur jwtFilter ko
                        // (jo hamnwe filterChain hamne banaya hai)
                        // UsernamePasswordAuthenticationFilter se pehle apply
                        // karna hai.
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //AuthenticationManager bean ko configure kiya, taki ham apne controller me
    // authenticationManager ko inject kar sakein.
    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}