package com.ciberfisicos1.trazabilidad.model.robot;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Robot_Id")
    private Long robotId;
    @Column(name = "Name") 
    private String name;
    @NotNull
    @Column(name = "Type",nullable = false)
    private String type;

    // Getters y Setters
    
}
