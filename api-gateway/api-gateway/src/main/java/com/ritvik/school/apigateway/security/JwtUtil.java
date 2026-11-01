package com.ritvik.school.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );//              |
        //                |
        //                V
        //converts your JWT secret into a cryptographic(unreadable
        // format so only people with the right key can read it)key
        // that can be used to verify the JWT signature.

        //Java represents secret as a String, but cryptographic
        // signing operations work with bytes.So we did,
        // secret.getBytes(StandardCharsets.UTF_8) to convert
        // the secret string into a byte array using UTF-8 encoding.

        //Keys is a utility class provided by the JJWT library.
        //hmacShaKeyFor() takes the byte array and creates a
        //Java cryptographic SecretKey suitable for HMAC-based
        // JWT signing algorithms.

        //In your implementation, that key is then used by the JWT
        // parser to verify signatures.
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            this.extractClaims(token);
            return true;
        } catch (io.jsonwebtoken.JwtException |
                 IllegalArgumentException ex) {
            return false;
        }
    }
}