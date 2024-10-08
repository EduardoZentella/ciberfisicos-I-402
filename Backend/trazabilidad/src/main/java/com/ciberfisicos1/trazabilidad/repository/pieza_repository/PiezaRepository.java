package com.ciberfisicos1.trazabilidad.repository.pieza_repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.pieza.Pieza;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;

@Repository

public interface PiezaRepository extends JpaRepository<Pieza, Long> {
    List<Pieza> findByLote_LoteId(Long loteId);
     default List<Pieza> findByType(String type,EncryptionService encryptionService) {
        List<Pieza> piezas = findAll();
        return piezas.stream()
                .filter(pieza -> {
                    String decryptedStatus = encryptionService.decryptData(pieza.getType(), 999L);
                    return type.equals(decryptedStatus);
                })
                .collect(Collectors.toList());
    }
}
