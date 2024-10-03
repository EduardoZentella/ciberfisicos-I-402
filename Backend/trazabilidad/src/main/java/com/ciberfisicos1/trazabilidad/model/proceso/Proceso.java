package com.ciberfisicos1.trazabilidad.model.proceso;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.fasterxml.jackson.annotation.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Proceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Proceso_Id")
    private Long procesoId;

    @NotNull
    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @OneToMany(mappedBy = "proceso", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonBackReference
    private List<Tarea> tareas = new ArrayList<>();

    @ManyToMany(mappedBy = "procesos")
    @Builder.Default
    @JsonIgnore
    private List<Robot> robots = new ArrayList<>();

    // Getters y Setters
}
