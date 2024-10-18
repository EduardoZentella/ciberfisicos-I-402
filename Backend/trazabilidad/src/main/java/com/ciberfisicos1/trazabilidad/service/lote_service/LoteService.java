package com.ciberfisicos1.trazabilidad.service.lote_service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.model.dto.LoteDTO;
import com.ciberfisicos1.trazabilidad.model.lote.Lote;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.repository.lote_repository.LoteRepository;
import com.ciberfisicos1.trazabilidad.repository.proceso_repository.ProcesoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final ProcesoRepository procesoRepository;

    public ResponseEntity<List<LoteDTO>> getAllLotes() {
        List<Lote> lotes = loteRepository.findAll();
        List<LoteDTO> loteDTOs = lotes.stream().map(Lote::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(loteDTOs);
    }

    public ResponseEntity<LoteDTO> getLoteById(Long loteId) {
        Optional<Lote> lote = loteRepository.findById(loteId);
        return ResponseEntity.ok(lote.map(Lote::toDTO).orElseThrow(() -> new ResourceNotFoundException("Lote no encontrado con id: " + loteId)));
    }

    public ResponseEntity<List<LoteDTO>> getLoteByProcesoId(Long procesoId) {
        List<Lote> lotes = loteRepository.findByProceso_ProcesoId(procesoId);
        if (lotes.isEmpty()) {
            throw new ResourceNotFoundException("Proceso no encontrado con id: " + procesoId);
        }
        List<LoteDTO> loteDTOs = lotes.stream().map(Lote::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(loteDTOs);
    }

    public ResponseEntity<LoteDTO> addLote(Map<String, Object> loteMap) {
        Lote lote = new Lote();
        if(loteMap.get("loteId") == null) {
            lote.setLoteId(((Number) loteMap.get("loteId")).longValue());
        }
        if (loteMap.get("procesoId") != null) {
            Long procesoId = ((Number) loteMap.get("procesoId")).longValue();
            Optional<Proceso> proceso = procesoRepository.findById(procesoId);
            if (proceso.isPresent()) {
                lote.setProceso(proceso.get());
            } else {
                throw new ResourceNotFoundException("Proceso no encontrado con id: " + procesoId);
            }
        }
        Lote savedlote = loteRepository.save(lote);
        return ResponseEntity.ok(savedlote.toDTO());
    }

    public ResponseEntity<LoteDTO> updateLote(Map<String, Object> loteMap, Long loteId) {
        Optional<Lote> existingLote = loteRepository.findById(loteId);
        if (existingLote.isPresent()) {
            Lote updatedLote = existingLote.get();
            copyNonNullProperties(loteMap, updatedLote);
            Lote savedLote = loteRepository.save(updatedLote);
            return ResponseEntity.ok(savedLote.toDTO());
        } else {
            throw new ResourceNotFoundException("Lote no encontrado con id: " + loteId);
        }
    }

    public ResponseEntity<Boolean> deleteLoteById(Long loteId) {
        try {
            loteRepository.deleteById(loteId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Map<String, Object> source, Lote target) {
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
}