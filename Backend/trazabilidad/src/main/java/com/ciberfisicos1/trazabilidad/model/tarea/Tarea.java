package com.ciberfisicos1.trazabilidad.model.tarea;

import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import com.ciberfisicos1.trazabilidad.model.actividad.Actividad;

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
    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "Proceso_Id", referencedColumnName = "Proceso_Id", nullable = false)
    @JsonIgnore
    private Proceso proceso;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Actividad> actividades = new ArrayList<>();

    @ManyToMany(mappedBy = "tareas")
    @Builder.Default
    @JsonIgnore
    private List<Robot> robots = new ArrayList<>();

    // Getters y Setters

}
