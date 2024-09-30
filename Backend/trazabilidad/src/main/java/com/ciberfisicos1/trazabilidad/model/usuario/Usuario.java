package com.ciberfisicos1.trazabilidad.model.usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Usuario_Id")
    private Long usuarioId;
    @NotNull
    @Column(name = "Email", nullable = false)
    private String email;
    @NotNull
    @Column(name = "Contraseña", nullable = false)
    private String contraseña;
    @Column(name = "Role")
    private String role;
    @Column(name = "Master_Key")
    private String masterKey;
    @Column(name = "Master_Key_Version")
    private String masterKeyVersion;

    // Getters y Setters
}
