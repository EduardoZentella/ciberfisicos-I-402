
package com.ciberfisicos1.trazabilidad.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;
import java.security.Key;


@Component
public class JwtUtil {

   private final Key secretKey;

    public JwtUtil() {
        this.secretKey = generateSecretKey();
    }

    private Key generateSecretKey() {
        byte[] keyBytes = new byte[64]; // 512 bits
        new java.security.SecureRandom().nextBytes(keyBytes);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getSigningKey() {
        byte[] encodedKey = secretKey.getEncoded();
        byte[] bytes = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(encodedKey));
        return new SecretKeySpec(bytes, "HmacSHA256");
    }


    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String email) {
        String token = createToken(email);
        return token;
    }

    private String createToken(String subject) {
        return Jwts.builder().subject(subject).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey()).compact();
    }

    public Boolean validateToken(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }
}