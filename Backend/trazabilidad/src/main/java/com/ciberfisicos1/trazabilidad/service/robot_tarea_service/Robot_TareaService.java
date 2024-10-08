package com.ciberfisicos1.trazabilidad.service.robot_tarea_service;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.model.robot_tarea.ID_Robot_Tarea;
import com.ciberfisicos1.trazabilidad.model.robot_tarea.Robot_Tarea;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.ciberfisicos1.trazabilidad.repository.robot_repository.RobotRepository;
import com.ciberfisicos1.trazabilidad.repository.robot_tarea_repository.Robot_TareaRepository;
import com.ciberfisicos1.trazabilidad.repository.tarea_repository.TareaRepository;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Robot_TareaService {

    private final Robot_TareaRepository robotTareaRepository;
    private final EncryptionService encryptionService;
    private final RobotRepository robotRepository;
    private final TareaRepository tareaRepository;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<Robot_Tarea>> getAllRobotTareas() {
        List<Robot_Tarea> robotTareas = robotTareaRepository.findAll();
        robotTareas.parallelStream().forEach(this::decryptRobotTarea);
        return ResponseEntity.ok(robotTareas);
    }

    public ResponseEntity<Robot_Tarea> getRobotTareaById(ID_Robot_Tarea id) {
        Optional<Robot_Tarea> robotTarea = robotTareaRepository.findById(id);
        robotTarea.ifPresent(this::decryptRobotTarea);
        return robotTarea.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Robot_Tarea>> getRobotTareasByRobotId(Long robotId) {
        List<Robot_Tarea> robotTareas = robotTareaRepository.findById_RobotId(robotId);
        robotTareas.parallelStream().forEach(this::decryptRobotTarea);
        return ResponseEntity.ok(robotTareas);
    }

    public ResponseEntity<List<Robot_Tarea>> getRobotTareasByTareaId(Long tareaId) {
        List<Robot_Tarea> robotTareas = robotTareaRepository.findById_TareaId(tareaId);
        robotTareas.parallelStream().forEach(this::decryptRobotTarea);
        return ResponseEntity.ok(robotTareas);
    }

    public ResponseEntity<Robot_Tarea> addRobotTarea(Robot_Tarea robotTarea) {
        Long robotId = robotTarea.getId().getRobotId();
        Long tareaId = robotTarea.getId().getTareaId();
        Optional<Robot> robot = robotRepository.findById(robotId);
        Optional<Tarea> tarea = tareaRepository.findById(tareaId);

        if (robot.isPresent() && tarea.isPresent()) {
            robotTarea.setRobot(robot.get());
            robotTarea.setTarea(tarea.get());

            encryptRobotTarea(robotTarea);
            Robot_Tarea savedRobotTarea = robotTareaRepository.save(robotTarea);
            decryptRobotTarea(savedRobotTarea);
            return ResponseEntity.ok(savedRobotTarea);
        } else {
            throw new ResourceNotFoundException("Robot o Tarea no encontrado");
        }
    }

    public ResponseEntity<Robot_Tarea> updateRobotTarea(Robot_Tarea robotTarea, ID_Robot_Tarea id) {
        Optional<Robot_Tarea> existingRobotTarea = robotTareaRepository.findById(id);
        if (existingRobotTarea.isPresent()) {
            Robot_Tarea updatedRobotTarea = existingRobotTarea.get();
            decryptRobotTarea(updatedRobotTarea);
            copyNonNullProperties(robotTarea, updatedRobotTarea);
            encryptRobotTarea(updatedRobotTarea);
            Robot_Tarea savedRobotTarea = robotTareaRepository.save(updatedRobotTarea);
            decryptRobotTarea(savedRobotTarea);
            return ResponseEntity.ok(savedRobotTarea);
        } else {
            throw new ResourceNotFoundException("Robot_Tarea no encontrado con id: " + id);
        }
    }

    public ResponseEntity<Boolean> deleteRobotTareaById(ID_Robot_Tarea id) {
        try {
            robotTareaRepository.deleteById(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Robot_Tarea source, Robot_Tarea target) {
        if (source.getIniDate() != null) {
            target.setIniDate(source.getIniDate());
        }
        if (source.getEndDate() != null) {
            target.setEndDate(source.getEndDate());
        }
        if (source.getStatus() != null) {
            target.setStatus(source.getStatus());
        }
    }

    private void encryptRobotTarea(Robot_Tarea robotTarea) {
        if (robotTarea.getStatus() == null || robotTarea.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del Robot_Tarea no puede estar vacío o nulo");
        } else {
            robotTarea.setStatus(encryptionService.encryptData(robotTarea.getStatus(), SYSTEM_USER_ID));
        }
    }

    private void decryptRobotTarea(Robot_Tarea robotTarea) {
        if (robotTarea.getStatus() == null || robotTarea.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del Robot_Tarea no puede estar vacío o nulo");
        } else {
            robotTarea.setStatus(encryptionService.decryptData(robotTarea.getStatus(), SYSTEM_USER_ID));
        }
    }
}
