package com.ciberfisicos1.trazabilidad.model.robot_actividad;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Robot_Actividad{
    @EmbeddedId
    private ID_Robot_Actividad id;
    @Column(name = "Ini_Date")
    private Date iniDate;
    @Column(name = "End_Date")
    private Date endDate;
    @Column(name = "Status")
    private String status;
}
