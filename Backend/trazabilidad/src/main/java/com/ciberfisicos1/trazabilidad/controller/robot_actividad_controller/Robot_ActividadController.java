package com.ciberfisicos1.trazabilidad.controller.robot_actividad_controller;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.ciberfisicos1.trazabilidad.model.robot_actividad.ID_Robot_Actividad;
import com.ciberfisicos1.trazabilidad.model.robot_actividad.Robot_Actividad;
import com.ciberfisicos1.trazabilidad.service.robot_actividad_service.Robot_ActividadService;

@RestController
@RequestMapping("/api/robot_actividades")
@RequiredArgsConstructor
public class Robot_ActividadController {

    private final Robot_ActividadService robotActividadService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Robot_Actividad>> getAllRobotActividades() {
        return robotActividadService.getAllRobotActividades();
    }

    @GetMapping("/{robotId}/{actividadId}")
    public ResponseEntity<Robot_Actividad> getRobotActividadById(@PathVariable Long robotId, @PathVariable Long actividadId) {
        ID_Robot_Actividad id = new ID_Robot_Actividad(robotId, actividadId);
        return robotActividadService.getRobotActividadById(id);
    }

    @GetMapping("/robot/{robotId}")
    public ResponseEntity<List<Robot_Actividad>> getRobotActividadesByRobotId(@PathVariable Long robotId) {
        return robotActividadService.getRobotActividadesByRobotId(robotId);
    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<Robot_Actividad>> getRobotActividadesByActividadId(@PathVariable Long actividadId) {
        return robotActividadService.getRobotActividadesByActividadId(actividadId);
    }

    @PostMapping
    public ResponseEntity<Robot_Actividad> addRobotActividad(@RequestBody Robot_Actividad robotActividad) {
        return robotActividadService.addRobotActividad(robotActividad);
    }

    @PutMapping("/{robotId}/{actividadId}")
    public ResponseEntity<Robot_Actividad> updateRobotActividad(@RequestBody Robot_Actividad robotActividad, @PathVariable Long robotId, @PathVariable Long actividadId) {
        ID_Robot_Actividad id = new ID_Robot_Actividad(robotId, actividadId);
        return robotActividadService.updateRobotActividad(robotActividad, id);
    }

    @DeleteMapping("/{robotId}/{actividadId}")
    public ResponseEntity<Boolean> deleteRobotActividadById(@PathVariable Long robotId, @PathVariable Long actividadId, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar Robot_Actividades.");
            }

            ID_Robot_Actividad id = new ID_Robot_Actividad(robotId, actividadId);
            return robotActividadService.deleteRobotActividadById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}

