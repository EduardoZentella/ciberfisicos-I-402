package com.ciberfisicos1.trazabilidad.model.historial;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class HistorialId implements Serializable {
    @Column(name = "Usuario_Id")
    private Long usuarioId;
    @Column(name = "Historial_Id")
    private Long id;

    // Getters, Setters, hashCode, equals
}

