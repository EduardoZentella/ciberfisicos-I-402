package com.ciberfisicos1.trazabilidad.service.tarea_service;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.ciberfisicos1.trazabilidad.repository.tarea_repository.TareaRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;
import com.ciberfisicos1.trazabilidad.repository.proceso_repository.ProcesoRepository;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final ProcesoRepository procesoRepository;
    private final EncryptionService encryptionService;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<Tarea>> getAllTareas() {
        List<Tarea> tareas = tareaRepository.findAll();
        tareas.forEach(this::decryptTarea);
        return ResponseEntity.ok(tareas);
    }

    public ResponseEntity<Tarea> getTareaById(Long tareaId) {
        Optional<Tarea> tarea = tareaRepository.findById(tareaId);
        tarea.ifPresent(this::decryptTarea);
        return tarea.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Tarea> addTarea(Map<String, Object> tareaMap) {
        Tarea tarea = new Tarea();
        tarea.setName((String) tareaMap.get("name"));
        tarea.setDescription((String) tareaMap.get("description"));
        if (tareaMap.get("proceso") != null) {
            Long procesoId = ((Number) tareaMap.get("proceso")).longValue();
            Optional<Proceso> proceso = procesoRepository.findById(procesoId);
            if (proceso.isPresent()) {
                tarea.setProceso(proceso.get());
            } else {
                throw new ResourceNotFoundException("Proceso no encontrado con id: " + procesoId);
            }
        }
        encryptTarea(tarea);
        Tarea savedTarea = tareaRepository.save(tarea);
        decryptTarea(savedTarea);
        return ResponseEntity.ok(savedTarea);
    }

    public ResponseEntity<Tarea> updateTarea(Map<String, Object> tareaMap, Long tareaId) {
        Optional<Tarea> existingTarea = tareaRepository.findById(tareaId);
        if (existingTarea.isPresent()) {
            Tarea updatedTarea = existingTarea.get();
            decryptTarea(updatedTarea);
            copyNonNullProperties(tareaMap, updatedTarea);
            encryptTarea(updatedTarea);
            Tarea savedTarea = tareaRepository.save(updatedTarea);
            decryptTarea(savedTarea);
            return ResponseEntity.ok(savedTarea);
        } else {
            throw new ResourceNotFoundException("Tarea no encontrada con id: " + tareaId);
        }
    }

    public ResponseEntity<Boolean> deleteTareaById(Long tareaId) {
        try {
            tareaRepository.deleteById(tareaId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Map<String, Object> source, Tarea target) {
        if (source.get("name") != null) {
            target.setName((String) source.get("name"));
        }
        if (source.get("description") != null) {
            target.setDescription((String) source.get("description"));
        }
        if (source.get("proceso") != null) {
            Long procesoId = ((Number) source.get("proceso")).longValue();
            Optional<Proceso> proceso = procesoRepository.findById(procesoId);
            if (proceso.isPresent()) {
                target.setProceso(proceso.get());
            } else {
                throw new ResourceNotFoundException("Proceso no encontrado con id: " + procesoId);
            }
        }
    }

    private void encryptTarea(Tarea tarea) {
        if (tarea.getName() == null || tarea.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la tarea no puede estar vacío o nulo");
        } else {
            tarea.setName(encryptionService.encryptData(tarea.getName(), SYSTEM_USER_ID));
        }

        if (tarea.getDescription() == null || tarea.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tarea no puede estar vacía o nula");
        } else {
            tarea.setDescription(encryptionService.encryptData(tarea.getDescription(), SYSTEM_USER_ID));
        }
    }

    private void decryptTarea(Tarea tarea) {
        if (tarea.getName() == null || tarea.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la tarea no puede estar vacío o nulo");
        } else {
            tarea.setName(encryptionService.decryptData(tarea.getName(), SYSTEM_USER_ID));
        }

        if (tarea.getDescription() == null || tarea.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tarea no puede estar vacía o nula");
        } else {
            tarea.setDescription(encryptionService.decryptData(tarea.getDescription(), SYSTEM_USER_ID));
        }
    }
}
