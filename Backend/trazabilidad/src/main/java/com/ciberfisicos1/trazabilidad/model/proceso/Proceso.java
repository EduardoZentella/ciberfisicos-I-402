package com.ciberfisicos1.trazabilidad.model.proceso;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import com.ciberfisicos1.trazabilidad.model.lote.Lote;
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

    @Nonnull
    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "Ini_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date iniDate;
    @Column(name = "End_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    @Column(name = "Status")
    private String status;

    @OneToMany(mappedBy = "proceso", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonBackReference(value = "proceso-tareas")
    private List<Tarea> tareas = new ArrayList<>();

    @OneToMany(mappedBy = "proceso", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonBackReference(value = "proceso-lotes")
    private List<Lote> lotes = new ArrayList<>();

    @ManyToMany(mappedBy = "procesos")
    @Builder.Default
    @JsonIgnore
    private List<Robot> robots = new ArrayList<>();

    // Getters y Setters
}
