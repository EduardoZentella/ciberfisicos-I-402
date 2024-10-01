package com.ciberfisicos1.trazabilidad.service.robot_actividad_service;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;
import com.ciberfisicos1.trazabilidad.model.actividad.Actividad;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.model.robot_actividad.ID_Robot_Actividad;
import com.ciberfisicos1.trazabilidad.model.robot_actividad.Robot_Actividad;
import com.ciberfisicos1.trazabilidad.repository.actividad_repository.ActividadRepository;
import com.ciberfisicos1.trazabilidad.repository.robot_actividad_repository.Robot_ActividadRepository;
import com.ciberfisicos1.trazabilidad.repository.robot_repository.RobotRepository;

@Service
@RequiredArgsConstructor
public class Robot_ActividadService {

    private final Robot_ActividadRepository robotActividadRepository;
    private final RobotRepository robotRepository;
    private final ActividadRepository actividadRepository;
    private final EncryptionService encryptionService;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<Robot_Actividad>> getAllRobotActividades() {
        List<Robot_Actividad> robotActividades = robotActividadRepository.findAll();
        robotActividades.forEach(this::decryptRobotActividad);
        return ResponseEntity.ok(robotActividades);
    }

    public ResponseEntity<Robot_Actividad> getRobotActividadById(ID_Robot_Actividad id) {
        Optional<Robot_Actividad> robotActividad = robotActividadRepository.findById(id);
        robotActividad.ifPresent(this::decryptRobotActividad);
        return robotActividad.map(ResponseEntity::ok)
                             .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Robot_Actividad>> getRobotActividadesByRobotId(Long robotId) {
        List<Robot_Actividad> robotActividades = robotActividadRepository.findById_RobotId(robotId);
        robotActividades.forEach(this::decryptRobotActividad);
        return ResponseEntity.ok(robotActividades);
    }

    public ResponseEntity<List<Robot_Actividad>> getRobotActividadesByActividadId(Long actividadId) {
        List<Robot_Actividad> robotActividades = robotActividadRepository.findById_ActividadId(actividadId);
        robotActividades.forEach(this::decryptRobotActividad);
        return ResponseEntity.ok(robotActividades);
    }

    public ResponseEntity<Robot_Actividad> addRobotActividad(Robot_Actividad robotActividad) {
        Long robotId = robotActividad.getId().getRobotId();
        Long actividadId = robotActividad.getId().getActividadId();
        Optional<Robot> robot = robotRepository.findById(robotId);
        Optional<Actividad> actividad = actividadRepository.findById(actividadId);

        if (robot.isPresent() && actividad.isPresent()) {
            robotActividad.setRobot(robot.get());
            robotActividad.setActividad(actividad.get());

            encryptRobotActividad(robotActividad);
            Robot_Actividad savedRobotActividad = robotActividadRepository.save(robotActividad);
            decryptRobotActividad(savedRobotActividad);
            return ResponseEntity.ok(savedRobotActividad);
        } else {
            throw new ResourceNotFoundException("Robot o Actividad no encontrado");
        }
    }

    public ResponseEntity<Robot_Actividad> updateRobotActividad(Robot_Actividad robotActividadMap, ID_Robot_Actividad id) {
        Optional<Robot_Actividad> existingRobotActividad = robotActividadRepository.findById(id);
        if (existingRobotActividad.isPresent()) {
            Robot_Actividad updatedRobotActividad = existingRobotActividad.get();
            decryptRobotActividad(updatedRobotActividad);
            copyNonNullProperties(robotActividadMap, updatedRobotActividad);
            encryptRobotActividad(updatedRobotActividad);
            Robot_Actividad savedRobotActividad = robotActividadRepository.save(updatedRobotActividad);
            decryptRobotActividad(savedRobotActividad);
            return ResponseEntity.ok(savedRobotActividad);
        } else {
            throw new ResourceNotFoundException("Robot_Actividad no encontrado con id: " + id);
        }
    }

    public ResponseEntity<Boolean> deleteRobotActividadById(ID_Robot_Actividad id) {
        try {
            robotActividadRepository.deleteById(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Robot_Actividad source, Robot_Actividad target) {
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

    private void encryptRobotActividad(Robot_Actividad robotActividad) {
        if (robotActividad.getStatus() == null || robotActividad.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del Robot_Actividad no puede estar vacío o nulo");
        } else {
            robotActividad.setStatus(encryptionService.encryptData(robotActividad.getStatus(), SYSTEM_USER_ID));
        }
    }

    private void decryptRobotActividad(Robot_Actividad robotActividad) {
        if (robotActividad.getStatus() == null || robotActividad.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del Robot_Actividad no puede estar vacío o nulo");
        } else {
            robotActividad.setStatus(encryptionService.decryptData(robotActividad.getStatus(), SYSTEM_USER_ID));
        }
    }
}
