package com.ciberfisicos1.trazabilidad.model.robot_actividad;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ID_Robot_Actividad implements Serializable {
    
    @Column(name = "Robot_Id", updatable = false, nullable = false)
    private Long robotId;

    @Column(name = "Actividad_Id", updatable = false, nullable = false)
    private Long actividadId;
}