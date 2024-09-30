package com.ciberfisicos1.trazabilidad.model.proceso;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Proceso_Id")
    private Long procesoId;
    @NotNull
    @Column(name = "Name",nullable = false)
    private String name;
    @Column(name = "Description")
    private String description;

    // Getters y Setters
    
}
