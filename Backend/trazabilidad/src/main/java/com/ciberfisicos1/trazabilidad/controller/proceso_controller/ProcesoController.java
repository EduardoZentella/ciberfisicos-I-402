package com.ciberfisicos1.trazabilidad.controller.proceso_controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.proceso_service.ProcesoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/procesos")
@RequiredArgsConstructor
public class ProcesoController {

    private final ProcesoService procesoService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Proceso>> getAllProcesos() {
        return procesoService.getAllProcesos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proceso> getProcesoById(@PathVariable Long id) {
        return procesoService.getProcesoById(id);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Proceso>> getProcesoByStatus() {
        return procesoService.getProcesoByStatus();
    }

    @GetMapping("/lastHours/{hours}")
    public ResponseEntity<List<Proceso>> getProcesosFromLastHours(@PathVariable int hours) {
        return procesoService.getProcesosFromLastHours(hours);
    }

    @PostMapping
    public ResponseEntity<Proceso> addProceso(@RequestBody Proceso proceso) {
        return procesoService.addProceso(proceso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proceso> updateProceso(@RequestBody Proceso proceso, @PathVariable Long id) {
        return procesoService.updateProceso(proceso, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProcesoById(@PathVariable Long id, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar procesos.");
            }

            return procesoService.deleteProcesoById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}
