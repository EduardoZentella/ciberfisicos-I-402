package com.ciberfisicos1.trazabilidad.controller.robot_tarea_controller;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.robot_tarea.ID_Robot_Tarea;
import com.ciberfisicos1.trazabilidad.model.robot_tarea.Robot_Tarea;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.robot_tarea_service.Robot_TareaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/robot_tareas")
@RequiredArgsConstructor
public class Robot_TareaController {

    private final Robot_TareaService robotTareaService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Robot_Tarea>> getAllRobotTareas() {
        return robotTareaService.getAllRobotTareas();
    }

    @GetMapping("/{robotId}/{tareaId}")
    public ResponseEntity<Robot_Tarea> getRobotTareaById(@PathVariable Long robotId, @PathVariable Long tareaId) {
        ID_Robot_Tarea id = new ID_Robot_Tarea(robotId, tareaId);
        return robotTareaService.getRobotTareaById(id);
    }

    @GetMapping("/robot/{robotId}")
    public ResponseEntity<List<Robot_Tarea>> getRobotTareasByRobotId(@PathVariable Long robotId) {
        return robotTareaService.getRobotTareasByRobotId(robotId);
    }

    @GetMapping("/tarea/{tareaId}")
    public ResponseEntity<List<Robot_Tarea>> getRobotTareasByTareaId(@PathVariable Long tareaId) {
        return robotTareaService.getRobotTareasByTareaId(tareaId);
    }

    @PostMapping
    public ResponseEntity<Robot_Tarea> addRobotTarea(@RequestBody  Robot_Tarea robotTarea) {
        return robotTareaService.addRobotTarea(robotTarea);
    }

    @PutMapping("/{robotId}/{tareaId}")
    public ResponseEntity<Robot_Tarea> updateRobotTarea(@RequestBody  Robot_Tarea robotTarea, @PathVariable Long robotId, @PathVariable Long tareaId) {
        ID_Robot_Tarea id = new ID_Robot_Tarea(robotId, tareaId);
        return robotTareaService.updateRobotTarea(robotTarea, id);
    }

    @DeleteMapping("/{robotId}/{tareaId}")
    public ResponseEntity<Boolean> deleteRobotTareaById(@PathVariable Long robotId, @PathVariable Long tareaId, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar Robot_Tareas.");
            }

            ID_Robot_Tarea id = new ID_Robot_Tarea(robotId, tareaId);
            return robotTareaService.deleteRobotTareaById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}
