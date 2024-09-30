package com.ciberfisicos1.trazabilidad.repository.robot_actividad_repository;

import com.ciberfisicos1.trazabilidad.model.robot_actividad.ID_Robot_Actividad;
import com.ciberfisicos1.trazabilidad.model.robot_actividad.Robot_Actividad;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Robot_ActividadRepository extends JpaRepository<Robot_Actividad, ID_Robot_Actividad>  {
    List<Robot_Actividad> findById_RobotId(Long RobotId);
    List<Robot_Actividad> findById_ActividadId(Long ActividadId);
    Optional<Robot_Actividad> findById_RobotIdAndId_ActividadId(Long RobotId, Long ActividadId);
}