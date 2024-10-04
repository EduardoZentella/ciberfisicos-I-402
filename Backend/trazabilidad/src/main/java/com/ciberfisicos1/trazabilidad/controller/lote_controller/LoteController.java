package com.ciberfisicos1.trazabilidad.controller.lote_controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.dto.LoteDTO;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.lote_service.LoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lotes")
@RequiredArgsConstructor
public class LoteController {

    private final LoteService loteService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<LoteDTO>> getAllLotes() {
        return loteService.getAllLotes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteDTO> getLoteById(@PathVariable Long id) {
        return loteService.getLoteById(id);
    }

    @GetMapping("/proceso/{procesoId}")
    public ResponseEntity<List<LoteDTO>> getLoteByProcesoId(@PathVariable Long procesoId) {
        return loteService.getLoteByProcesoId(procesoId);
    }

    @PostMapping
    public ResponseEntity<LoteDTO> addLote(@RequestBody Map<String, Object> loteMap) {
        return loteService.addLote(loteMap);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoteDTO> updateLote(@RequestBody Map<String, Object> loteMap, @PathVariable Long id) {
        return loteService.updateLote(loteMap, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteLoteById(@PathVariable Long id, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar actividades.");
            }

            return loteService.deleteLoteById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}
