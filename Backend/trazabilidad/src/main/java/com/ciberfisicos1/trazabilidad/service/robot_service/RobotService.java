package com.ciberfisicos1.trazabilidad.service.robot_service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.repository.robot_repository.RobotRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RobotService {

    private final RobotRepository robotRepository;
    private final EncryptionService encryptionService;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<Robot>> getAllRobots() {
        List<Robot> robots = robotRepository.findAll();
        robots.forEach(this::decryptRobot);
        return ResponseEntity.ok(robots);
    }

    public ResponseEntity<Robot> getRobotById(Long robotId) {
        Optional<Robot> robot = robotRepository.findById(robotId);
        robot.ifPresent(this::decryptRobot);
        return robot.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Robot> addRobot(Robot robot) {
        encryptRobot(robot);
        Robot savedRobot = robotRepository.save(robot);
        decryptRobot(savedRobot);
        return ResponseEntity.ok(savedRobot);
    }

    public ResponseEntity<Robot> updateRobot(Robot robot, Long robotId) {
        Optional<Robot> existingRobot = robotRepository.findById(robotId);
        if (existingRobot.isPresent()) {
            Robot updatedRobot = existingRobot.get();
            decryptRobot(updatedRobot);
            copyNonNullProperties(robot, updatedRobot);
            encryptRobot(updatedRobot);
            Robot savedRobot = robotRepository.save(updatedRobot);
            decryptRobot(savedRobot);
            return ResponseEntity.ok(savedRobot);
        } else {
            throw new ResourceNotFoundException("Robot no encontrado con id: " + robotId);
        }
    }

    public ResponseEntity<Boolean> deleteRobotById(Long robotId) {
        try {
            robotRepository.deleteById(robotId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Robot source, Robot target) {
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getType() != null) {
            target.setType(source.getType());
        }
    }

    private void encryptRobot(Robot robot) {
        if (robot.getName() == null || robot.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del robot no puede estar vacío o nulo");
        } else {
            robot.setName(encryptionService.encryptData(robot.getName(), SYSTEM_USER_ID));
        }

        if (robot.getType() == null || robot.getType().isEmpty()) {
            throw new IllegalArgumentException("El tipo del robot no puede estar vacío o nulo");
        } else {
            robot.setType(encryptionService.encryptData(robot.getType(), SYSTEM_USER_ID));
        }
    }

    private void decryptRobot(Robot robot) {
        if (robot.getName() == null || robot.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del robot no puede estar vacío o nulo");
        } else {
            robot.setName(encryptionService.decryptData(robot.getName(), SYSTEM_USER_ID));
        }

        if (robot.getType() == null || robot.getType().isEmpty()) {
            throw new IllegalArgumentException("El tipo del robot no puede estar vacío o nulo");
        } else {
            robot.setType(encryptionService.decryptData(robot.getType(), SYSTEM_USER_ID));
        }
    }
}
