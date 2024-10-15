package com.ciberfisicos1.trazabilidad.service.proceso_service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
        procesos.parallelStream().forEach(this::decryptProceso);
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

    public ResponseEntity<List<Proceso>> getProcesoByStatus(int type) {
        String status;
        switch (type) {
            case 1:
                status = "Done";
                break;
            case 2:
                status = "In Progress";
                break;
            case 3:
                status = "Error";
                break;
            default:
                throw new IllegalArgumentException("Tipo de estado de proceso no válido");
        }
        List<Proceso> procesos = procesoRepository.findProcesoByStatusDecrypted(status,encryptionService);
        procesos.parallelStream().forEach(this::decryptProceso);
        return ResponseEntity.ok(procesos);
    }

    public ResponseEntity<List<Proceso>> getProcesosFromLastHours(int hours) {
        Optional<Proceso> mostRecentProceso = procesoRepository.findMostRecentProceso();
        if (mostRecentProceso.isPresent()) {
            Date mostRecentDate = mostRecentProceso.get().getIniDate();
            Date startDate = new Date(mostRecentDate.getTime() - hours * 60 * 60 * 1000);
            List<Proceso> procesos = procesoRepository.findProcesosFromDate(startDate);
            procesos.parallelStream().forEach(this::decryptProceso);
            return ResponseEntity.ok(procesos);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    public ResponseEntity<List<Proceso>> getProcesosFromDate(String date){
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
        Date endDate = calculateEndDate(startDate, date);
        List<Proceso> procesos = procesoRepository.findProcesosFromDateRange(startDate,endDate);
        procesos.parallelStream().forEach(this::decryptProceso);
        return ResponseEntity.ok(procesos);
    }

    public ResponseEntity<Proceso> updateProceso(Proceso proceso, Long procesoId) {
        Optional<Proceso> existingProceso = procesoRepository.findById(procesoId);
        if (existingProceso.isPresent()) {
            Proceso updatedProceso = existingProceso.get();
            decryptProceso(updatedProceso);
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
        if  (source.getLocation() != null) {
            target.setLocation(source.getLocation());
        }
        if (source.getIniDate() != null) {
            target.setIniDate(source.getIniDate());
        }
        if (source.getEndDate() != null) {
            target.setEndDate(source.getEndDate());
        }
        if (source.getStatus() != null) {
            target.setStatus(source.getStatus());
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

        if (proceso.getLocation() == null || proceso.getLocation().isEmpty()) {
            throw new IllegalArgumentException("La ubicación del proceso no puede estar vacía o nula");
        } else {
            proceso.setLocation(encryptionService.encryptData(proceso.getLocation(), SYSTEM_USER_ID));
        }

        if (proceso.getStatus() == null || proceso.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del proceso no puede estar vacío o nulo");
        } else {
            proceso.setStatus(encryptionService.encryptData(proceso.getStatus(), SYSTEM_USER_ID));
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

        if (proceso.getLocation() == null || proceso.getLocation().isEmpty()) {
            throw new IllegalArgumentException("La ubicación del proceso no puede estar vacía o nula");
        } else {
            proceso.setLocation(encryptionService.decryptData(proceso.getLocation(), SYSTEM_USER_ID));
        }

        if (proceso.getStatus() == null || proceso.getStatus().isEmpty()) {
            throw new IllegalArgumentException("El estado del proceso no puede estar vacío o nulo");
        } else {
            proceso.setStatus(encryptionService.decryptData(proceso.getStatus(), SYSTEM_USER_ID));
        }
    }
    
    private Date calculateEndDate(Date startDate, String formatUsed) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    switch (formatUsed) {
        case "yyyy":
            calendar.add(Calendar.YEAR, 1);
            break;
        case "yyyy-MM":
        case "yyyy/MM":
        case "yyyy.MM":
            calendar.add(Calendar.MONTH, 1);
            break;
        case "yyyy-MM-dd":
        case "yyyy/MM/dd":
        case "yyyy.MM.dd":
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            break;
    }
    return calendar.getTime();
    }
}
