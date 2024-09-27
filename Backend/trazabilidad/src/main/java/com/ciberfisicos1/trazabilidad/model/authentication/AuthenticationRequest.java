package com.ciberfisicos1.trazabilidad.model.authentication;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {

    @NotNull
    private String email;

    @NotNull
    private String contraseña;

    // Getters y Setters
}
