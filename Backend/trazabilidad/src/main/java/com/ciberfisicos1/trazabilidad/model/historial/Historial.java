package com.ciberfisicos1.trazabilidad.model.historial;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Historial {
    
    @EmbeddedId
    private HistorialId historialId;

    @NotNull
    @Column(nullable = false)
    private String contraseña;

    @NotNull
    @Column(nullable = false)
    private String masterKey;

    @NotNull
    @Column(nullable = false)
    private String version;

    // Getters y Setters
}