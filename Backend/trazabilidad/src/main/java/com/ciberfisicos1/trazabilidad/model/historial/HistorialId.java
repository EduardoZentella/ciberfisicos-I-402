package com.ciberfisicos1.trazabilidad.model.historial;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.*;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class HistorialId implements Serializable {
    private Long usuarioId;
    private Long id;

    // Getters, Setters, hashCode, equals
}

