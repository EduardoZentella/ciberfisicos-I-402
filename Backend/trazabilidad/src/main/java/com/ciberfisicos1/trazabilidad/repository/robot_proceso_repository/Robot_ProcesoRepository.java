package com.ciberfisicos1.trazabilidad.repository.robot_proceso_repository;

import com.ciberfisicos1.trazabilidad.model.robot_proceso.ID_Robot_Proceso;
import com.ciberfisicos1.trazabilidad.model.robot_proceso.Robot_Proceso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Robot_ProcesoRepository extends JpaRepository<Robot_Proceso, ID_Robot_Proceso>  {
    List<Robot_Proceso> findById_RobotId(Long RobotId);
    List<Robot_Proceso> findById_ProcesoId(Long ProcesoId);
    Optional<Robot_Proceso> findById_RobotIdAndId_ProcesoId(Long RobotId, Long ProcesoId);
}
