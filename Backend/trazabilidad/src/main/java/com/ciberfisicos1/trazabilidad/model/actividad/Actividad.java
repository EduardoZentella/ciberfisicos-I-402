package com.ciberfisicos1.trazabilidad.model.actividad;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.ciberfisicos1.trazabilidad.model.dto.ActividadDTO;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.fasterxml.jackson.annotation.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Actividad_Id")
    private Long actividadId;

    @Nonnull
    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    @Nonnull
    @JoinColumn(name = "Tarea_Id", referencedColumnName = "Tarea_Id", nullable = false)
    @JsonManagedReference
    private Tarea tarea;

    @ManyToMany(mappedBy = "actividades")
    @Builder.Default
    @JsonIgnore
    private List<Robot> robots = new ArrayList<>();

    public ActividadDTO toDTO() {
        ActividadDTO dto = new ActividadDTO();
        dto.setActividadId(this.actividadId);
        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setTareaId(this.tarea.getTareaId());
        return dto;
    }
    
    // Getters y Setters
}
