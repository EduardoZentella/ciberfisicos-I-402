package com.ciberfisicos1.trazabilidad.repository.proceso_repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;

@Repository
public interface ProcesoRepository extends JpaRepository<Proceso, Long> {
    @Query("SELECT p FROM Proceso p WHERE p.status != 'Done'")
    List<Proceso> findProcesoByStatus();

    default List<Proceso> findProcesoByStatusDecrypted(EncryptionService encryptionService) {
        List<Proceso> procesos = findProcesoByStatus();
        return procesos.stream()
                .filter(proceso -> {
                    String decryptedStatus = encryptionService.decryptData(proceso.getStatus(), 999L);
                    return !"Done".equals(decryptedStatus);
                })
                .collect(Collectors.toList());
    }
}