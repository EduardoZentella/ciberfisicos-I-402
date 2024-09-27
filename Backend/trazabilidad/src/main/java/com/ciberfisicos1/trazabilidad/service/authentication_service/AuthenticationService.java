package com.ciberfisicos1.trazabilidad.service.authentication_service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.UnauthorizedException;
import com.ciberfisicos1.trazabilidad.model.authentication.AuthenticationRequest;
import com.ciberfisicos1.trazabilidad.model.authentication.AuthenticationResponse;
import com.ciberfisicos1.trazabilidad.model.historial.Historial;
import com.ciberfisicos1.trazabilidad.model.historial.HistorialId;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.historial_repository.HistorialRepository;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.security.jwt.JwtUtil;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final HistorialRepository historialRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        // Buscar el usuario por email
        Usuario usuario = usuarioRepository.findByEmail(authenticationRequest.getEmail());
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        // Usar el email del usuario para autenticar
        String email = usuario.getEmail();
        // Verificar la contraseña
        if (!passwordEncoder.matches(authenticationRequest.getContraseña(), usuario.getContraseña())) {
            throw new UnauthorizedException("Contraseña incorrecta");
        }     

        // Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, authenticationRequest.getContraseña())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return AuthenticationResponse.builder().token(jwt).build();
    }

    public ResponseEntity<String> register(Usuario usuario) {
        // Encriptar la contraseña del usuario
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        // Generar y encriptar la MasterKey
        String masterKey = encryptionService.generateMasterKey();
        String encryptedMasterKey = encryptionService.encryptMasterKey(masterKey, usuario.getContraseña());
        // Guardar la MasterKey encriptada en el usuario
        usuario.setMasterKey(encryptedMasterKey);
        usuario.setMasterKeyVersion("V1");
        // Guardar el usuario en la base de datos
        usuarioRepository.save(usuario);

        // Obtener el último `id` para el `usuarioId` y generar un nuevo `id`
        Long ultimoId = historialRepository.findMaxIdByUsuarioId(usuario.getId()).orElse(0L);
        Long nuevoId = ultimoId + 1;

        // Guardar en el historial
        Historial historial = Historial.builder()
            .historialId(new HistorialId(usuario.getId(), nuevoId))
            .contraseña(usuario.getContraseña())
            .masterKey(encryptedMasterKey)
            .version("V1")
            .build();
        historialRepository.save(historial);

        return ResponseEntity.ok("Usuario registrado con éxito");
    }

    public ResponseEntity<String> register2(HttpServletRequest request) {
        // Crear un nuevo usuario con los valores específicos
        Usuario usuario = Usuario.builder()
                .id(999L)
                .email("SYSTEM")
                .contraseña("12345")
                .role("S_ADMIN")
                .masterKeyVersion("V1")
                .build();

        // Encriptar la contraseña del usuario
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));

        // Generar y encriptar la MasterKey
        String masterKey = encryptionService.generateMasterKey();
        String encryptedMasterKey = encryptionService.encryptMasterKey(masterKey, usuario.getContraseña());

        // Guardar la MasterKey encriptada en el usuario
        usuario.setMasterKey(encryptedMasterKey);

        // Guardar el usuario en la base de datos
        usuarioRepository.save(usuario);
        
        // Obtener el último `id` para el `usuarioId` y generar un nuevo `id`
        Long ultimoId = historialRepository.findMaxIdByUsuarioId(usuario.getId()).orElse(0L);
        Long nuevoId = ultimoId + 1;

        // Guardar en el historial
        Historial historial = Historial.builder()
            .historialId(new HistorialId(usuario.getId(), nuevoId))
            .contraseña(usuario.getContraseña())
            .masterKey(encryptedMasterKey)
            .version("V1")
            .build();
        historialRepository.save(historial);

        return ResponseEntity.ok("Usuario registrado con éxito");
    }
}