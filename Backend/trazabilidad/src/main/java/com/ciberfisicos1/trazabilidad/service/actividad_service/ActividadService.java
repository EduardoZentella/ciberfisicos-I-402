package com.ciberfisicos1.trazabilidad.service.actividad_service;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.model.actividad.Actividad;
import com.ciberfisicos1.trazabilidad.repository.actividad_repository.ActividadRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final EncryptionService encryptionService;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<Actividad>> getAllActividades() {
        List<Actividad> actividades = actividadRepository.findAll();
        actividades.forEach(this::decryptActividad);
        return ResponseEntity.ok(actividades);
    }

    public ResponseEntity<Actividad> getActividadById(Long actividadId) {
        Optional<Actividad> actividad = actividadRepository.findById(actividadId);
        actividad.ifPresent(this::decryptActividad);
        return actividad.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Actividad> addActividad(Actividad actividad) {
        encryptActividad(actividad);
        Actividad savedActividad = actividadRepository.save(actividad);
        decryptActividad(savedActividad);
        return ResponseEntity.ok(savedActividad);
    }

    public ResponseEntity<Actividad> updateActividad(Actividad actividad, Long actividadId) {
        Optional<Actividad> existingActividad = actividadRepository.findById(actividadId);
        if (existingActividad.isPresent()) {
            Actividad updatedActividad = existingActividad.get();
            copyNonNullProperties(actividad, updatedActividad);
            encryptActividad(updatedActividad);
            Actividad savedActividad = actividadRepository.save(updatedActividad);
            decryptActividad(savedActividad);
            return ResponseEntity.ok(savedActividad);
        } else {
            throw new ResourceNotFoundException("Actividad no encontrada con id: " + actividadId);
        }
    }

    public ResponseEntity<Boolean> deleteActividadById(Long actividadId) {
        try {
            actividadRepository.deleteById(actividadId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Actividad source, Actividad target) {
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }
    }

    private void encryptActividad(Actividad actividad) {
        if (actividad.getName() == null || actividad.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la actividad no puede estar vacío o nulo");
        } else {
            actividad.setName(encryptionService.encryptData(actividad.getName(), SYSTEM_USER_ID));
        }

        if (actividad.getDescription() == null || actividad.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la actividad no puede estar vacía o nula");
        } else {
            actividad.setDescription(encryptionService.encryptData(actividad.getDescription(), SYSTEM_USER_ID));
        }
    }

    private void decryptActividad(Actividad actividad) {
        if (actividad.getName() == null || actividad.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la actividad no puede estar vacío o nulo");
        } else {
            actividad.setName(encryptionService.decryptData(actividad.getName(), SYSTEM_USER_ID));
        }

        if (actividad.getDescription() == null || actividad.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la actividad no puede estar vacía o nula");
        } else {
            actividad.setDescription(encryptionService.decryptData(actividad.getDescription(), SYSTEM_USER_ID));
        }
    }
}

