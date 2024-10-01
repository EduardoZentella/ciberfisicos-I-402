package com.ciberfisicos1.trazabilidad.service.robot_proceso_service;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.model.robot_proceso.ID_Robot_Proceso;
import com.ciberfisicos1.trazabilidad.model.robot_proceso.Robot_Proceso;
import com.ciberfisicos1.trazabilidad.repository.proceso_repository.ProcesoRepository;
import com.ciberfisicos1.trazabilidad.repository.robot_proceso_repository.Robot_ProcesoRepository;
import com.ciberfisicos1.trazabilidad.repository.robot_repository.RobotRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Robot_ProcesoService {

    private final Robot_ProcesoRepository robotProcesoRepository;
    private final EncryptionService encryptionService;
    private final RobotRepository robotRepository;
    private final ProcesoRepository procesoRepository;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<Robot_Proceso>> getAllRobotProcesos() {
        List<Robot_Proceso> robotProcesos = robotProcesoRepository.findAll();
        robotProcesos.forEach(this::decryptRobotProceso);
        return ResponseEntity.ok(robotProcesos);
    }

    public ResponseEntity<Robot_Proceso> getRobotProcesoById(ID_Robot_Proceso id) {
        Optional<Robot_Proceso> robotProceso = robotProcesoRepository.findById(id);
        robotProceso.ifPresent(this::decryptRobotProceso);
        return robotProceso.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Robot_Proceso>> getRobotProcesosByRobotId(Long robotId) {
        List<Robot_Proceso> robotProcesos = robotProcesoRepository.findById_RobotId(robotId);
        robotProcesos.forEach(this::decryptRobotProceso);
        return ResponseEntity.ok(robotProcesos);
    }

    public ResponseEntity<List<Robot_Proceso>> getRobotProcesosByProcesoId(Long procesoId) {
        List<Robot_Proceso> robotProcesos = robotProcesoRepository.findById_ProcesoId(procesoId);
        robotProcesos.forEach(this::decryptRobotProceso);
        return ResponseEntity.ok(robotProcesos);
    }   

    public ResponseEntity<Robot_Proceso> addRobotProceso(Robot_Proceso robotProceso) {
        Long robotId = robotProceso.getId().getRobotId();
        Long procesoId = robotProceso.getId().getProcesoId();
        Optional<Robot> robot = robotRepository.findById(robotId);
        Optional<Proceso> proceso = procesoRepository.findById(procesoId);

        if (robot.isPresent() && proceso.isPresent()) {
            robotProceso.setRobot(robot.get());
            robotProceso.setProceso(proceso.get());

            encryptRobotProceso(robotProceso);
            Robot_Proceso savedRobotProceso = robotProcesoRepository.save(robotProceso);
            decryptRobotProceso(savedRobotProceso);
            return ResponseEntity.ok(savedRobotProceso);
        } else {
            throw new ResourceNotFoundException("Robot o Proceso no encontrado");
        }
    }

    public ResponseEntity<Robot_Proceso> updateRobotProceso(Robot_Proceso robotProceso, ID_Robot_Proceso id) {
        Optional<Robot_Proceso> existingRobotProceso = robotProcesoRepository.findById(id);
        if (existingRobotProceso.isPresent()) {
            Robot_Proceso updatedRobotProceso = existingRobotProceso.get();
            decryptRobotProceso(updatedRobotProceso);
            copyNonNullProperties(robotProceso, updatedRobotProceso);
            encryptRobotProceso(updatedRobotProceso);
            Robot_Proceso savedRobotProceso = robotProcesoRepository.save(updatedRobotProceso);
            decryptRobotProceso(savedRobotProceso);
            return ResponseEntity.ok(savedRobotProceso);
        } else {
            throw new ResourceNotFoundException("Robot_Proceso no encontrado con id: " + id);
        }
    }

    public ResponseEntity<Boolean> deleteRobotProcesoById(ID_Robot_Proceso id) {
        try {
            robotProcesoRepository.deleteById(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Robot_Proceso source, Robot_Proceso target) {
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

    private void encryptRobotProceso(Robot_Proceso robotProceso) {
        if (robotProceso.getStatus() == null || robotProceso.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del Robot_Proceso no puede estar vacío o nulo");
        } else {
            robotProceso.setStatus(encryptionService.encryptData(robotProceso.getStatus(), SYSTEM_USER_ID));
        }
    }

    private void decryptRobotProceso(Robot_Proceso robotProceso) {
        if (robotProceso.getStatus() == null || robotProceso.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del Robot_Proceso no puede estar vacío o nulo");
        } else {
            robotProceso.setStatus(encryptionService.decryptData(robotProceso.getStatus(), SYSTEM_USER_ID));
        }
    }
}
