package com.ciberfisicos1.trazabilidad.controller.robot_controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.robot.Robot;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.robot_service.RobotService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/robots")
@RequiredArgsConstructor
public class RobotController {

    private final RobotService robotService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Robot>> getAllRobots() {
        return robotService.getAllRobots();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Robot> getRobotById(@PathVariable Long id) {
        return robotService.getRobotById(id);
    }

    @PostMapping
    public ResponseEntity<Robot> addRobot(@RequestBody Robot robot) {
        return robotService.addRobot(robot);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Robot> updateRobot(@RequestBody Robot robot, @PathVariable Long id) {
        return robotService.updateRobot(robot, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRobotById(@PathVariable Long id, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar robots.");
            }

            return robotService.deleteRobotById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}

