package com.ciberfisicos1.trazabilidad.model.actividad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Actividad_Id")
    private Long actividadId;

    @NotNull
    @Column(name = "Name", nullable = false)
    private String name;
    @Column(name = "Description")
    private String description;

    // Getters y Setters
    
}