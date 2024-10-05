package com.ciberfisicos1.trazabilidad.model.robot;

import java.util.*;
import com.ciberfisicos1.trazabilidad.model.actividad.Actividad;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
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
    @Nonnull
    @Column(name = "Type",nullable = false)
    private String type;
    @ManyToMany
    @JoinTable(
        name = "Robot_Proceso",
        joinColumns = @JoinColumn(name = "Robot_Id"),
        inverseJoinColumns = @JoinColumn(name = "Proceso_Id")
    )
    @Builder.Default
    @JsonIgnore
    private List<Proceso> procesos = new ArrayList<>();
    @ManyToMany
    @JoinTable(
        name = "Robot_Tarea",
        joinColumns = @JoinColumn(name = "Robot_Id"),
        inverseJoinColumns = @JoinColumn(name = "Tarea_Id")
    )
    @Builder.Default
    @JsonIgnore
    private List<Tarea> tareas = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "Robot_Actividad",
        joinColumns = @JoinColumn(name = "Robot_Id"),
        inverseJoinColumns = @JoinColumn(name = "Actividad_Id")
    )
    @Builder.Default
    @JsonIgnore
    private List<Actividad> actividades = new ArrayList<>();

    // Getters y Setters
    
}
