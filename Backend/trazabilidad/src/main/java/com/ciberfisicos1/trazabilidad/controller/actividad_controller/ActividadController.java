package com.ciberfisicos1.trazabilidad.controller.actividad_controller;

import java.util.Map;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.ciberfisicos1.trazabilidad.model.dto.ActividadDTO;
import com.ciberfisicos1.trazabilidad.service.actividad_service.ActividadService;

@RestController
@RequestMapping("/api/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<ActividadDTO>> getAllActividades() {
        return actividadService.getAllActividades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> getActividadById(@PathVariable Long id) {
        return actividadService.getActividadById(id);
    }

    @PostMapping
    public ResponseEntity<ActividadDTO> addActividad(@RequestBody Map<String, Object> actividad) {
        return actividadService.addActividad(actividad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadDTO> updateActividad(@RequestBody Map<String, Object> actividad, @PathVariable Long id) {
        return actividadService.updateActividad(actividad, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteActividadById(@PathVariable Long id, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar actividades.");
            }

            return actividadService.deleteActividadById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}

