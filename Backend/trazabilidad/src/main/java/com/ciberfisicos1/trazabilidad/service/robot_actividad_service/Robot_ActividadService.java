package com.ciberfisicos1.trazabilidad.service.robot_actividad_service;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;
import com.ciberfisicos1.trazabilidad.model.robot_actividad.ID_Robot_Actividad;
import com.ciberfisicos1.trazabilidad.model.robot_actividad.Robot_Actividad;
import com.ciberfisicos1.trazabilidad.repository.robot_actividad_repository.Robot_ActividadRepository;

@Service
@RequiredArgsConstructor
public class Robot_ActividadService {

    private final Robot_ActividadRepository robotActividadRepository;
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
        encryptRobotActividad(robotActividad);
        Robot_Actividad savedRobotActividad = robotActividadRepository.save(robotActividad);
        decryptRobotActividad(savedRobotActividad);
        return ResponseEntity.ok(savedRobotActividad);
    }

    public ResponseEntity<Robot_Actividad> updateRobotActividad(Robot_Actividad robotActividad, ID_Robot_Actividad id) {
        Optional<Robot_Actividad> existingRobotActividad = robotActividadRepository.findById(id);
        if (existingRobotActividad.isPresent()) {
            Robot_Actividad updatedRobotActividad = existingRobotActividad.get();
            copyNonNullProperties(robotActividad, updatedRobotActividad);
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
