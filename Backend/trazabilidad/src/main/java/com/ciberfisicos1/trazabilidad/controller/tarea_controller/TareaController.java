package com.ciberfisicos1.trazabilidad.controller.tarea_controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.dto.TareaDTO;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.tarea_service.TareaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<TareaDTO>> getAllTareas() {
        return tareaService.getAllTareas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> getTareaById(@PathVariable Long id) {
        return tareaService.getTareaById(id);
    }

    @GetMapping("/last/{n}")
    public ResponseEntity<List<TareaDTO>> getLastNTareas(@PathVariable int n) {
        return tareaService.getLastNTareas(n);
    }

    @GetMapping("date/{date}")
    public ResponseEntity<List<TareaDTO>> getTareasByDate(@PathVariable String date) {
        return tareaService.getTareasByDate(date);
    }

    @PostMapping
    public ResponseEntity<TareaDTO> addTarea(@RequestBody Map<String, Object> tarea) {
        return tareaService.addTarea(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> updateTarea(@RequestBody Map<String, Object> tarea, @PathVariable Long id) {
        return tareaService.updateTarea(tarea, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTareaById(@PathVariable Long id, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar tareas.");
            }

            return tareaService.deleteTareaById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}
