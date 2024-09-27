package com.ciberfisicos1.trazabilidad.service.usuario_service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.ciberfisicos1.trazabilidad.model.historial.Historial;
import com.ciberfisicos1.trazabilidad.model.historial.HistorialId;
import com.ciberfisicos1.trazabilidad.model.usuario.Usuario;
import com.ciberfisicos1.trazabilidad.repository.historial_repository.HistorialRepository;
import com.ciberfisicos1.trazabilidad.repository.usuario_repository.UsuarioRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final HistorialRepository historialRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;

    public ResponseEntity<List<Usuario>> getUsuarioList() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    public ResponseEntity<Usuario> getUsuarioById(long id) {
        Optional<Usuario> data = usuarioRepository.findById(id);
        return data.map(usuario -> ResponseEntity.ok(usuario))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Usuario> getUsuarioByEmail(String email) {
        Optional<Usuario> data = Optional.of(usuarioRepository.findByEmail(email));
        return data.map(usuario -> ResponseEntity.ok(usuario))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Usuario> updateUsuario(Usuario usuario, Long id_usuario) throws Exception {
        
        Optional<Usuario> data = usuarioRepository.findById(id_usuario);
        if (data.isPresent()) {
            Usuario existingUsuario = data.get();
            copyNonNullProperties(usuario, existingUsuario);

            Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
            return ResponseEntity.ok(updatedUsuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Boolean> deleteUsuarioById(Long id_usuario) {
        try {
            usuarioRepository.deleteById(id_usuario);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void copyNonNullProperties(Usuario source, Usuario target) throws Exception {
        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }
        if (source.getRole() != null) {
            target.setRole(source.getRole());
        }
        if (source.getContraseña() != null) {
            // Guardar la contraseña actual antes de actualizarla
            String currentPassword = target.getContraseña();
            // Actualizar la contraseña del usuario
            String newPassword = passwordEncoder.encode(source.getContraseña());
            target.setContraseña(newPassword);

            // Re-encriptar las contraseñas en el historial con la nueva contraseña
            List<Historial> historialList = historialRepository.findByHistorialIdUsuarioId(target.getId());
            for (Historial historial : historialList) {
                // Desencriptar la contraseña antigua
                String decryptedOldPassword = passwordEncoder.matches(historial.getContraseña(), currentPassword) ? source.getContraseña() : null;
                if (decryptedOldPassword != null) {
                    // Re-encriptar la contraseña con la nueva contraseña
                    String encryptedNewPassword = passwordEncoder.encode(decryptedOldPassword);
                    historial.setContraseña(encryptedNewPassword);
                    historialRepository.save(historial);
                }
            }

            // Generar y encriptar la nueva MasterKey
            String masterKey = encryptionService.generateMasterKey();
            String encryptedMasterKey = encryptionService.encryptMasterKey(masterKey, source.getContraseña());
            target.setMasterKey(encryptedMasterKey);

            // Incrementar la versión de la MasterKey
            String currentVersion = target.getMasterKeyVersion();
            String newVersion = incrementVersion(currentVersion);
            target.setMasterKeyVersion(newVersion);

            // Obtener el último `id` para el `usuarioId` y generar un nuevo `id`
            Long ultimoId = historialRepository.findMaxIdByUsuarioId(source.getId()).orElse(0L);
            Long nuevoId = ultimoId + 1;

            // Guardar en el historial
            Historial historial = Historial.builder()
                .historialId(new HistorialId(source.getId(), nuevoId))
                .contraseña(source.getContraseña())
                .masterKey(encryptedMasterKey)
                .version("V1")
                .build();
            historialRepository.save(historial);
        }
    }

    private String incrementVersion(String currentVersion) {
        if (currentVersion != null && currentVersion.startsWith("V")) {
            try {
                int versionNumber = Integer.parseInt(currentVersion.substring(1));
                return "V" + (versionNumber + 1);
            } catch (NumberFormatException e) {
                // Handle the case where the version is not a valid number
                return "V1";
            }
        }
        return "V1";
    }
}

