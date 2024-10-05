package com.ciberfisicos1.trazabilidad.model.historial;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Historial {
    
    @EmbeddedId
    private HistorialId historialId;

    @Nonnull
    @Column(name = "Contraseña", nullable = false)
    private String contraseña;

    @Nonnull
    @Column(name = "Master_Key", nullable = false)
    private String masterKey;

    @Nonnull
    @Column(name = "Version", nullable = false)
    private String version;

    // Getters y Setters
}