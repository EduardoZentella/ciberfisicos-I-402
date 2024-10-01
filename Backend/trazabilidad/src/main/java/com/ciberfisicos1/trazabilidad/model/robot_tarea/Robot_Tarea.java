package com.ciberfisicos1.trazabilidad.model.robot_tarea;

import java.util.Date;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Robot_Tarea{
    @EmbeddedId
    private ID_Robot_Tarea id;
    @ManyToOne
    @MapsId("robotId")
    @JoinColumn(name = "Robot_Id")
    @JsonIgnore
    private Robot robot;
    @ManyToOne
    @MapsId("tareaId")
    @JoinColumn(name = "Tarea_Id")
    @JsonIgnore
    private Tarea tarea;
    @Column(name = "Ini_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date iniDate;
    @Column(name = "End_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    @Column(name = "Status")
    private String status;
}
