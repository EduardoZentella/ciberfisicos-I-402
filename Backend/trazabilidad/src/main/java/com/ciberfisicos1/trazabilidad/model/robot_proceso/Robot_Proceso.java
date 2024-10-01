package com.ciberfisicos1.trazabilidad.model.robot_proceso;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Robot_Proceso {

    @EmbeddedId
    private ID_Robot_Proceso id;

    @ManyToOne
    @MapsId("robotId")
    @JoinColumn(name = "Robot_Id")
    @JsonIgnore
    private Robot robot;
    @ManyToOne
    @MapsId("procesoId")
    @JoinColumn(name = "Proceso_Id")
    @JsonIgnore
    private Proceso proceso;
    @Column(name = "Ini_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date iniDate;
    @Column(name = "End_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    @Column(name = "Status")
    private String status;
}
