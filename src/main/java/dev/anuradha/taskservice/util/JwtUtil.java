package dev.anuradha.taskservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key key;

    // Initialize the secure Key after the secret is injected
    @PostConstruct
    public void init() {
        // Generate a secure HMAC-SHA256 key with a proper length of 256 bits (32 bytes)
        if (SECRET_KEY != null && !SECRET_KEY.isEmpty()) {
            byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
            this.key = Keys.hmacShaKeyFor(decodedKey);  // Use the correct key length
        } else {
            // Fallback to generating a key with the correct length if SECRET_KEY is not provided
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // Generates a 256-bit key
        }
    }
    public String extractUsernameFromHeader(HttpServletRequest request) {
        return extractClaim(request, Claims::getSubject);
    }

    public String extractUsername(String token) {
        Claims claims = getAllClaims(token);
        return claims.getSubject();
    }


    public String extractRoleFromHeader(HttpServletRequest request) {
        return extractClaim(request, claims -> claims.get("role", String.class));
    }

    private <T> T extractClaim(HttpServletRequest request, Function<Claims, T> claimsResolver) {
        String token = getToken(request);
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid token");
        }
        return header.substring(7);
    }

    public boolean isAdmin(String token) {
        Claims claims = getAllClaims(token);
        List<String> roles = claims.get("roles", List.class);
        return roles != null && roles.contains("ROLE_ADMIN");
    }
}
