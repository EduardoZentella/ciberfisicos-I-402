package com.ciberfisicos1.trazabilidad.controller.usuario_controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.usuario_service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios() {
        return usuarioService.getUsuarioList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable long id) {
        return usuarioService.getUsuarioById(id);
    }

    @GetMapping("/email")
    public ResponseEntity<Usuario> getUsuarioByEmail(@RequestParam String email) {
        return usuarioService.getUsuarioByEmail(email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario, @PathVariable Long id, HttpServletRequest request) throws Exception {
        // Verificar si el usuario autenticado es un administrador o el mismo usuario
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null) {
                throw new UnauthorizedException("Acceso denegado: no autenticado.");
            }

            Optional<Usuario> data = usuarioRepository.findById(id);
            if (data.isPresent()) {
                Usuario existingUsuario = data.get();

                // Verificar permisos
                if (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN") && !authUser.getId().equals(id)) {
                    throw new ForbiddenException("Acceso denegado: solo los administradores y el mismo usuario pueden alterar usuarios.");
                }

                // Solo un S_ADMIN puede modificar a otro S_ADMIN
                if (existingUsuario.getRole().equals("S_ADMIN") && !authUser.getRole().equals("S_ADMIN")) {
                    throw new ForbiddenException("Acceso denegado: solo un S_ADMIN puede modificar a otro S_ADMIN.");
                }

                return usuarioService.updateUsuario(usuario, id);
            } else {
                throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
            }
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUsuarioById(@PathVariable Long id, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden eliminar usuarios.");
            }

            return usuarioService.deleteUsuarioById(id);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }
}
