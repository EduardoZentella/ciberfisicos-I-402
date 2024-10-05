package com.ciberfisicos1.trazabilidad.service.pieza_service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ciberfisicos1.trazabilidad.repository.lote_repository.LoteRepository;
import com.ciberfisicos1.trazabilidad.repository.pieza_repository.PiezaRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;
import com.ciberfisicos1.trazabilidad.model.pieza.Pieza;
import com.ciberfisicos1.trazabilidad.errors.exceptions.ResourceNotFoundException;
import com.ciberfisicos1.trazabilidad.model.dto.PiezaDTO;
import com.ciberfisicos1.trazabilidad.model.lote.Lote;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PiezaService {

    private final PiezaRepository piezaRepository;
    private final LoteRepository loteRepository;
    private final EncryptionService encryptionService;
    private static final Long SYSTEM_USER_ID = 999L;

    public ResponseEntity<List<PiezaDTO>> getAllPiezas() {
        List<Pieza> piezas = piezaRepository.findAll();
        piezas.forEach(this::decryptPieza);
        List<PiezaDTO> piezasDTOs = piezas.stream().map(Pieza::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(piezasDTOs);
    }

    public ResponseEntity<PiezaDTO> getPiezaById(Long piezaId) {
        Optional<Pieza> pieza = piezaRepository.findById(piezaId);
        pieza.ifPresent(this::decryptPieza);
        return ResponseEntity.ok(pieza.map(Pieza::toDTO).orElseThrow(() -> new ResourceNotFoundException("Pieza no encontrada con id: " + piezaId)));
    }

    public ResponseEntity<List<PiezaDTO>> getPiezaByLoteId(Long loteId) {
        List<Pieza> piezas = piezaRepository.findByLoteId(loteId);
        piezas.forEach(this::decryptPieza);
        if (piezas.isEmpty()) {
            throw new ResourceNotFoundException("Lote no encontrado con id: " + loteId);
        }
        List<PiezaDTO> piezasDTOs = piezas.stream().map(Pieza::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(piezasDTOs);
    }

    public ResponseEntity<PiezaDTO> addPieza(Map<String, Object> piezaMap) {
        Pieza pieza = new Pieza();
        if(piezaMap.get("piezaId") == null) {
            pieza.setPiezaId(((Number) piezaMap.get("piezaId")).longValue());
        }
        pieza.setType((String) piezaMap.get("type"));
        if (piezaMap.get("lote") != null) {
            Long loteId = ((Number) piezaMap.get("lote")).longValue();
            Optional<Lote> lote = loteRepository.findById(loteId);
            if (lote.isPresent()) {
                pieza.setLote(lote.get());
            } else {
                throw new ResourceNotFoundException("Lote no encontrada con id: " + loteId);
            }
        }
        encryptPieza(pieza);
        Pieza savedPieza = piezaRepository.save(pieza);
        decryptPieza(savedPieza);
        return ResponseEntity.ok(savedPieza.toDTO());
    }

    public ResponseEntity<PiezaDTO> updatePieza(Map<String, Object> piezaMap, Long piezaId) {
        Optional<Pieza> existingPieza = piezaRepository.findById(piezaId);
        if (existingPieza.isPresent()) {
            Pieza updatedPieza = existingPieza.get();
            decryptPieza(updatedPieza);
            copyNonNullProperties(piezaMap, updatedPieza);
            encryptPieza(updatedPieza);
            Pieza savedPieza = piezaRepository.save(updatedPieza);
            decryptPieza(savedPieza);
            return ResponseEntity.ok(savedPieza.toDTO());
        } else {
            throw new ResourceNotFoundException("Pieza no encontrada con id: " + piezaId);
        }
    }

    public ResponseEntity<Boolean> deletePiezaById(Long piezaId) {
        try {
            piezaRepository.deleteById(piezaId);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private void copyNonNullProperties(Map<String, Object> source, Pieza target) {
        if (source.get("type") != null) {
            target.setType((String) source.get("type"));
        }
        if (source.get("lote") != null) {
            Long loteId = ((Number) source.get("lote")).longValue();
            Optional<Lote> lote = loteRepository.findById(loteId);
            if (lote.isPresent()) {
                target.setLote(lote.get());
            } else {
                throw new ResourceNotFoundException("Lote no encontrada con id: " + loteId);
            }
        }
    }

    private void encryptPieza(Pieza pieza) {
        if (pieza.getType() == null || pieza.getType().isEmpty()) {
            throw new IllegalArgumentException("El tipo de la pieza no puede estar vacío o nulo");
        } else {
            pieza.setType(encryptionService.encryptData(pieza.getType(), SYSTEM_USER_ID));
        }
    }

    private void decryptPieza(Pieza pieza) {
        if (pieza.getType() == null || pieza.getType().isEmpty()) {
            throw new IllegalArgumentException("El tipo de la pieza no puede estar vacío o nulo");
        } else {
            pieza.setType(encryptionService.decryptData(pieza.getType(), SYSTEM_USER_ID));
        }
    }
}
