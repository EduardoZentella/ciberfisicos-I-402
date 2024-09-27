package com.ciberfisicos1.trazabilidad.controller.authentication_controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ciberfisicos1.trazabilidad.errors.exceptions.ForbiddenException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.authentication.AuthenticationRequest;
import com.ciberfisicos1.trazabilidad.service.authentication_service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

import com.ciberfisicos1.trazabilidad.model.authentication.AuthenticationResponse;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest) throws Exception {
        return ResponseEntity.ok(authenticationService.authenticate(authRequest)); 
    }

   @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario, HttpServletRequest request) {
        // Verificar si el usuario autenticado es un administrador
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtUtil.extractEmail(jwt);
            Usuario authUser = usuarioRepository.findByEmail(email);
            if (authUser == null || (!authUser.getRole().equals("ADMIN") && !authUser.getRole().equals("S_ADMIN"))) {
                throw new ForbiddenException("Acceso denegado: solo los administradores pueden registrar nuevos usuarios.");
            }
            return authenticationService.register(usuario);
        } else {
            throw new UnauthorizedException("Acceso denegado: no autenticado.");
        }
    }

    @PostMapping("/register2")
    public ResponseEntity<String> register2(HttpServletRequest request) throws Exception {
        return authenticationService.register2(request);
    }
}
