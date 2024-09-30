package com.ciberfisicos1.trazabilidad.model.robot_tarea;

import java.util.Date;
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
    @Column(name = "Ini_Date")
    private Date iniDate;
    @Column(name = "End_Date")
    private Date endDate;
    @Column(name = "Status")
    private String status;
}
