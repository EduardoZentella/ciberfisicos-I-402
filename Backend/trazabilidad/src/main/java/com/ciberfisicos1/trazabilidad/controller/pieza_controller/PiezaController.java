package com.ciberfisicos1.trazabilidad.controller.pieza_controller;

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
import com.ciberfisicos1.trazabilidad.model.dto.PiezaDTO;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.pieza_service.PiezaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/piezas")
@RequiredArgsConstructor
public class PiezaController {

    private final PiezaService piezaService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<PiezaDTO>> getAllPiezas() {
        return piezaService.getAllPiezas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PiezaDTO> getPiezaById(@PathVariable Long id) {
        return piezaService.getPiezaById(id);
    }

    @GetMapping("/lote/{loteId}")
    public ResponseEntity<List<PiezaDTO>> getPiezaByLoteId(@PathVariable Long loteId) {
        return piezaService.getPiezaByLoteId(loteId);
    }

    @PostMapping
    public ResponseEntity<PiezaDTO> addPieza(@RequestBody Map<String, Object> piezaMap) {
        return piezaService.addPieza(piezaMap);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PiezaDTO> updatePieza(@RequestBody Map<String, Object> piezaMap, @PathVariable Long id) {
        return piezaService.updatePieza(piezaMap, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePiezaById(@PathVariable Long id, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar actividades.");
            }

            return piezaService.deletePiezaById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}