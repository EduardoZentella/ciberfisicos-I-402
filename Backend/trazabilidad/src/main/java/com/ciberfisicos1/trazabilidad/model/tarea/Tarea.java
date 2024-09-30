package com.ciberfisicos1.trazabilidad.model.tarea;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Tarea_Id")
    private Long tareaId;
    @NotNull
    @Column(name = "Name",nullable = false)
    private String name;
    @Column(name = "Description")
    private String description;

    // Getters y Setters
    
}