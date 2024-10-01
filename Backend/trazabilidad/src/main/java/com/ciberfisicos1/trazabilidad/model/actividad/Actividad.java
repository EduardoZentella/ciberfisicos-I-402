package com.ciberfisicos1.trazabilidad.model.actividad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne
    @NotNull
    @JoinColumn(name = "Tarea_Id", referencedColumnName = "Tarea_Id", nullable = false)
    private Tarea tarea;

    @ManyToMany(mappedBy = "actividades")
    @Builder.Default
    @JsonIgnore
    private List<Robot> robots = new ArrayList<>();
}
