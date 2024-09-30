package com.ciberfisicos1.trazabilidad.model.robot_proceso;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ID_Robot_Proceso implements Serializable {
    
    @Column(name = "Robot_Id", updatable = false, nullable = false)
    private Long robotId;

    @Column(name = "Proceso_Id",updatable = false, nullable = false)
    private Long procesoId;
}