package com.ciberfisicos1.trazabilidad.repository.robot_tarea_repository;

import com.ciberfisicos1.trazabilidad.model.robot_tarea.ID_Robot_Tarea;
import com.ciberfisicos1.trazabilidad.model.robot_tarea.Robot_Tarea;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Robot_TareaRepository extends JpaRepository<Robot_Tarea, ID_Robot_Tarea>  {
    List<Robot_Tarea> findById_RobotId(Long RobotId);
    List<Robot_Tarea> findById_TareaId(Long TareaId);
    Optional<Robot_Tarea> findById_RobotIdAndId_TareaId(Long RobotId, Long TareaId);
}
