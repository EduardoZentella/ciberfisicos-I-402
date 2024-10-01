package com.ciberfisicos1.trazabilidad.model.robot_actividad;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import com.ciberfisicos1.trazabilidad.model.actividad.Actividad;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Robot_Actividad{
    @EmbeddedId
    private ID_Robot_Actividad id;
    @ManyToOne
    @MapsId("robotId")
    @JoinColumn(name = "Robot_Id")
    @JsonIgnore
    private Robot robot;
    @ManyToOne
    @MapsId("actividadId")
    @JoinColumn(name = "Actividad_Id")
    @JsonIgnore
    private Actividad actividad;
    @Column(name = "Ini_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date iniDate;
    @Column(name = "End_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    @Column(name = "Status")
    private String status;
}
