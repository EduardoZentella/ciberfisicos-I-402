package com.ciberfisicos1.trazabilidad.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;

@SpringBootTest
public class JwtUtilIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void whenGenerateToken_thenTokenIsValid() {
        // given
        String username = "test@example.com";

        // when
        String token = jwtUtil.generateToken(username);

        // then
        assertThat(jwtUtil.validateToken(token, username)).isTrue();
        System.out.println("Prueba de integración de JwtUtil exitosa: JWT generado y validado correctamente.");
    }
}
