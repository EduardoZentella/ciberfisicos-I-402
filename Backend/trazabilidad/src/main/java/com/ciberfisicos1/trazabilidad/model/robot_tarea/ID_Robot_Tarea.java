package com.ciberfisicos1.trazabilidad.model.robot_tarea;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ID_Robot_Tarea implements Serializable {
    
    @Column(name = "Robot_Id", updatable = false, nullable = false)
    private Long robotId;

    @Column(name = "Tarea_Id", updatable = false, nullable = false)
    private Long tareaId;
}
