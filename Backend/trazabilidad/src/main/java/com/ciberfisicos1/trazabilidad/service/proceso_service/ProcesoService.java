package com.ciberfisicos1.trazabilidad.service.proceso_service;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.repository.proceso_repository.ProcesoRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcesoService {

    private final ProcesoRepository procesoRepository;
    private final EncryptionService encryptionService;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<Proceso>> getAllProcesos() {
        List<Proceso> procesos = procesoRepository.findAll();
        procesos.forEach(this::decryptProceso);
        return ResponseEntity.ok(procesos);
    }

    public ResponseEntity<Proceso> getProcesoById(Long procesoId) {
        Optional<Proceso> proceso = procesoRepository.findById(procesoId);
        proceso.ifPresent(this::decryptProceso);
        return proceso.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Proceso> addProceso(Proceso proceso) {
        encryptProceso(proceso);
        Proceso savedProceso = procesoRepository.save(proceso);
        decryptProceso(savedProceso);
        return ResponseEntity.ok(savedProceso);
    }

    public ResponseEntity<Proceso> updateProceso(Proceso proceso, Long procesoId) {
        Optional<Proceso> existingProceso = procesoRepository.findById(procesoId);
        if (existingProceso.isPresent()) {
            Proceso updatedProceso = existingProceso.get();
            copyNonNullProperties(proceso, updatedProceso);
            encryptProceso(updatedProceso);
            Proceso savedProceso = procesoRepository.save(updatedProceso);
            decryptProceso(savedProceso);
            return ResponseEntity.ok(savedProceso);
        } else {
            throw new ResourceNotFoundException("Proceso no encontrado con id: " + procesoId);
        }
    }

    public ResponseEntity<Boolean> deleteProcesoById(Long procesoId) {
        try {
            procesoRepository.deleteById(procesoId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Proceso source, Proceso target) {
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }
    }

    private void encryptProceso(Proceso proceso) {
        if (proceso.getName() == null || proceso.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proceso no puede estar vacío o nulo");
        } else {
            proceso.setName(encryptionService.encryptData(proceso.getName(), SYSTEM_USER_ID));
        }
    
        if (proceso.getDescription() == null || proceso.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La descripción del proceso no puede estar vacía o nula");
        } else {
            proceso.setDescription(encryptionService.encryptData(proceso.getDescription(), SYSTEM_USER_ID));
        }
    }
    
    
    private void decryptProceso(Proceso proceso) {
        if (proceso.getName() == null || proceso.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proceso no puede estar vacío o nulo");
        } else {
            proceso.setName(encryptionService.decryptData(proceso.getName(), SYSTEM_USER_ID));
        }
    
        if (proceso.getDescription() == null || proceso.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La descripción del proceso no puede estar vacía o nula");
        } else {
            proceso.setDescription(encryptionService.decryptData(proceso.getDescription(), SYSTEM_USER_ID));
        }
    }    
}
