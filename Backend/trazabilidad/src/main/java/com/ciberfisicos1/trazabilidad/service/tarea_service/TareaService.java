package com.ciberfisicos1.trazabilidad.service.tarea_service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.errors.exceptions.IllegalArgumentException;
import com.ciberfisicos1.trazabilidad.model.dto.TareaDTO;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;
import com.ciberfisicos1.trazabilidad.repository.tarea_repository.TareaRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import lombok.RequiredArgsConstructor;
import com.ciberfisicos1.trazabilidad.repository.proceso_repository.ProcesoRepository;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final ProcesoRepository procesoRepository;
    private final EncryptionService encryptionService;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<TareaDTO>> getAllTareas() {
        List<Tarea> tareas = tareaRepository.findAll();
        tareas.parallelStream().forEach(this::decryptTarea);
        List<TareaDTO> tareaDTOs = tareas.stream().map(Tarea::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(tareaDTOs);
    }

    public ResponseEntity<TareaDTO> getTareaById(Long tareaId) {
        Optional<Tarea> tarea = tareaRepository.findById(tareaId);
        tarea.ifPresent(this::decryptTarea);
        return ResponseEntity.ok(tarea.map(Tarea::toDTO).orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + tareaId)));
    }

    public ResponseEntity<List<TareaDTO>> getLastNTareas(int n) {
        Pageable pageable = PageRequest.of(0, n);
        List<Tarea> tareas = tareaRepository.findTopNTareas(pageable);
        tareas.parallelStream().forEach(this::decryptTarea);
        List<TareaDTO> tareaDTOs = tareas.stream().map(Tarea::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(tareaDTOs);
    }

    public ResponseEntity<List<TareaDTO>> getTareasByDate(String date){
        List<String> dateFormats = List.of("yyyy", "yyyy-MM", "yyyy/MM", "yyyy.MM", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd");
        Date startDate = null;
        for (String format : dateFormats) {
            try {
                startDate = new SimpleDateFormat(format).parse(date);
                break;
            } catch (ParseException e) {
                // Continuar con el siguiente formato
            }
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Formato de fecha no válido. Los formatos válidos son: yyyy, yyyy-MM, yyyy/MM, yyyy.MM, yyyy-MM-dd, yyyy/MM/dd, yyyy.MM.dd.");
        }
        List<Tarea> tareas = tareaRepository.findTareaFromDate(startDate);
        tareas.parallelStream().forEach(this::decryptTarea);
        return ResponseEntity.ok(tareas.stream().map(Tarea::toDTO).collect(Collectors.toList()));
    }

    public ResponseEntity<TareaDTO> addTarea(Map<String, Object> tareaMap) {
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
        return ResponseEntity.ok(savedTarea.toDTO());
    }

    public ResponseEntity<TareaDTO> updateTarea(Map<String, Object> tareaMap, Long tareaId) {
        Optional<Tarea> existingTarea = tareaRepository.findById(tareaId);
        if (existingTarea.isPresent()) {
            Tarea updatedTarea = existingTarea.get();
            decryptTarea(updatedTarea);
            copyNonNullProperties(tareaMap, updatedTarea);
            encryptTarea(updatedTarea);
            Tarea savedTarea = tareaRepository.save(updatedTarea);
            decryptTarea(savedTarea);
            return ResponseEntity.ok(savedTarea.toDTO());
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
        if (tarea.getStatus() == null || tarea.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El statuto de la tarea no puede estar vaició o nulo ");
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
        if (tarea.getStatus() == null || tarea.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El statuto de la tarea no puede estar vaició o nulo ");
        }
    }
}
