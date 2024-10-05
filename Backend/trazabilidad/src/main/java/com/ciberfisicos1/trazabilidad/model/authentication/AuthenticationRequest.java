package com.ciberfisicos1.trazabilidad.model.authentication;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {

    @Nonnull
    private String email;

    @Nonnull
    private String contraseña;

    // Getters y Setters
}
