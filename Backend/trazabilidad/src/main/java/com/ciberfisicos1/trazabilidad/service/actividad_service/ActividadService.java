package com.ciberfisicos1.trazabilidad.service.actividad_service;

import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.model.actividad.Actividad;
import com.ciberfisicos1.trazabilidad.model.dto.ActividadDTO;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.ciberfisicos1.trazabilidad.repository.actividad_repository.ActividadRepository;
import com.ciberfisicos1.trazabilidad.repository.tarea_repository.TareaRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final TareaRepository tareaRepository;
    private final EncryptionService encryptionService;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<ActividadDTO>> getAllActividades() {
        List<Actividad> actividades = actividadRepository.findAll();
        actividades.parallelStream().forEach(this::decryptActividad);
        List<ActividadDTO> actividadesDTOs = actividades.stream().map(Actividad::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(actividadesDTOs);
    }

    public ResponseEntity<ActividadDTO> getActividadById(Long actividadId) {
        Optional<Actividad> actividad = actividadRepository.findById(actividadId);
        actividad.ifPresent(this::decryptActividad);
        return ResponseEntity.ok(actividad.map(Actividad::toDTO).orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + actividadId)));
    }

    public ResponseEntity<ActividadDTO> addActividad(Map<String, Object> actividadMap) {
        Actividad actividad = new Actividad();
        actividad.setName((String) actividadMap.get("name"));
        actividad.setDescription((String) actividadMap.get("description"));
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            if (actividadMap.get("iniDate") != null) {
                actividad.setIniDate(dateFormat.parse((String) actividadMap.get("iniDate")));
            }
            if (actividadMap.get("endDate") != null) {
                actividad.setEndDate(dateFormat.parse((String) actividadMap.get("endDate")));
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
        actividad.setStatus((String) actividadMap.get("status"));
        if (actividadMap.get("tareaId") != null) {
            Long tareaId = ((Number) actividadMap.get("tareaId")).longValue();
            Optional<Tarea> tarea = tareaRepository.findById(tareaId);
            if (tarea.isPresent()) {
                actividad.setTarea(tarea.get());
            } else {
                throw new ResourceNotFoundException("Tarea no encontrada con id: " + tareaId);
            }
        }
        encryptActividad(actividad);
        Actividad savedActividad = actividadRepository.save(actividad);
        decryptActividad(savedActividad);
        return ResponseEntity.ok(savedActividad.toDTO());
    }

    public ResponseEntity<ActividadDTO> updateActividad(Map<String, Object> actividadMap, Long actividadId) {
        Optional<Actividad> existingActividad = actividadRepository.findById(actividadId);
        if (existingActividad.isPresent()) {
            Actividad updatedActividad = existingActividad.get();
            decryptActividad(updatedActividad);
            copyNonNullProperties(actividadMap, updatedActividad);
            encryptActividad(updatedActividad);
            Actividad savedActividad = actividadRepository.save(updatedActividad);
            decryptActividad(savedActividad);
            return ResponseEntity.ok(savedActividad.toDTO());
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

    private void copyNonNullProperties(Map<String, Object> source, Actividad target) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if (source.get("name") != null) {
            target.setName((String) source.get("name"));
        }
        if (source.get("description") != null) {
            target.setDescription((String) source.get("description"));
        }
        if (source.get("iniDate") != null) {
            try {
                target.setIniDate(dateFormat.parse((String) source.get("iniDate")));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format for iniDate", e);
            }
        }
        if (source.get("endDate") != null) {
            try {
                target.setEndDate(dateFormat.parse((String) source.get("endDate")));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format for endDate", e);
            }
        }
        if (source.get("status") != null) {
            target.setStatus((String) source.get("status"));
        }
        if (source.get("tarea") != null) {
            Long tareaId = ((Number) source.get("tarea")).longValue();
            Optional<Tarea> tarea = tareaRepository.findById(tareaId);
            if (tarea.isPresent()) {
                target.setTarea(tarea.get());
            } else {
                throw new ResourceNotFoundException("Tarea no encontrada con id: " + tareaId);
            }
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
