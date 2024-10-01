package com.ciberfisicos1.trazabilidad.controller.robot_proceso_controller;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.robot_proceso.ID_Robot_Proceso;
import com.ciberfisicos1.trazabilidad.model.robot_proceso.Robot_Proceso;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.robot_proceso_service.Robot_ProcesoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/robot_procesos")
@RequiredArgsConstructor
public class Robot_ProcesoController {

    private final Robot_ProcesoService robotProcesoService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Robot_Proceso>> getAllRobotProcesos() {
        return robotProcesoService.getAllRobotProcesos();
    }

    @GetMapping("/{robotId}/{procesoId}")
    public ResponseEntity<Robot_Proceso> getRobotProcesoById(@PathVariable Long robotId, @PathVariable Long procesoId) {
        ID_Robot_Proceso id = new ID_Robot_Proceso(robotId, procesoId);
        return robotProcesoService.getRobotProcesoById(id);
    }

    @GetMapping("/robot/{robotId}")
    public ResponseEntity<List<Robot_Proceso>> getRobotProcesosByRobotId(@PathVariable Long robotId) {
        return robotProcesoService.getRobotProcesosByRobotId(robotId);
    }

    @GetMapping("/proceso/{procesoId}")
    public ResponseEntity<List<Robot_Proceso>> getRobotProcesosByProcesoId(@PathVariable Long procesoId) {
        return robotProcesoService.getRobotProcesosByProcesoId(procesoId);
    }

    @PostMapping
    public ResponseEntity<Robot_Proceso> addRobotProceso(@RequestBody Robot_Proceso robotProceso) {
        return robotProcesoService.addRobotProceso(robotProceso);
    }

    @PutMapping("/{robotId}/{procesoId}")
    public ResponseEntity<Robot_Proceso> updateRobotProceso(@RequestBody Robot_Proceso robotProceso, @PathVariable Long robotId, @PathVariable Long procesoId) {
        ID_Robot_Proceso id = new ID_Robot_Proceso(robotId, procesoId);
        return robotProcesoService.updateRobotProceso(robotProceso, id);
    }

    @DeleteMapping("/{robotId}/{procesoId}")
    public ResponseEntity<Boolean> deleteRobotProcesoById(@PathVariable Long robotId, @PathVariable Long procesoId, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar relaciones Robot_Proceso.");
            }

            ID_Robot_Proceso id = new ID_Robot_Proceso(robotId, procesoId);
            return robotProcesoService.deleteRobotProcesoById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}
