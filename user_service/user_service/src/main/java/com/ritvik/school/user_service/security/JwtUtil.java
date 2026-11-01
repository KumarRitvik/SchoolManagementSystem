package com.ritvik.school.user_service.security;

import com.ritvik.school.user_service.enums.RoleName;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {// this class generates token and extracts all fields from token for validation!

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    //token generate karne ke liye after authentication!..
    public String generateToken(String username, RoleName roleName) {

        Date now = new Date();
        return Jwts.builder().setSubject(username).claim("role", roleName).setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)),SignatureAlgorithm.HS256)
                //                |
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
                //converts your JWT secret into a cryptographic key that can be used to verify the JWT signature.

                .compact();
        //Our JWT contains:- subject = username, role= roleName,issuedAt,expiration. Our implementation
        // explicitly puts the username into the "JWT subject" and the role into the "role claim".
        //JWT
        // ├── AuthHeader
        // │     └── HS256,Bearer343nk3mo43ni3....(token)
        // ├── Payload/Body
        // │     ├── sub = admin    |
        // │     ├── role = ADMIN   |------------> All these fields under Claims(which is inside Payload)
        // │     ├── iat = ...      |
        // │     └── exp = ...      |
        // └── Signature
    }


    //Token se username(from subject),password, password, role(from claims), expiration, issuedAt
  // nikalne k liye ye sabhi below methods use karenege..!

    //to get claims from token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                   .build().parseClaimsJws(token).getBody();
    }
    //To get username from Token(through claims)
    public String extractUsername(String token) {return
            this.getClaims(token).getSubject();
    }

    //To get roles from Token(through claims)
    public String extractRole(String token) {
        return
                this.getClaims(token).get("role", String.class);
    }

    //To check if the token is Expired by extracting Expiration field from token(through Claims)
    private boolean isTokenExpired(String token) {
        return this.getClaims(token).getExpiration().before(new Date());
    }

    //To check Token's expiry
    public boolean isTokenValid(String token, String username) {
        try {
            String usernameFromToken= this.extractUsername(token);
            return username.equals(usernameFromToken) && !isTokenExpired(token);
        }
        catch (Exception e) {
            return false;
        }
    }

}