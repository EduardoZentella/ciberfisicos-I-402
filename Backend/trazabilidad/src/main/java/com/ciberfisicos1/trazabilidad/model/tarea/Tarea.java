package com.ciberfisicos1.trazabilidad.model.tarea;

import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.fasterxml.jackson.annotation.*;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import com.ciberfisicos1.trazabilidad.model.actividad.Actividad;
import com.ciberfisicos1.trazabilidad.model.dto.TareaDTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Tarea_Id")
    private Long tareaId;

    @Nonnull
    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    @Nonnull
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

    public TareaDTO toDTO() {
        TareaDTO dto = new TareaDTO();
        dto.setTareaId(this.tareaId);
        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setProcesoId(this.proceso.getProcesoId());
        return dto;
    }

    // Getters y Setters
}
