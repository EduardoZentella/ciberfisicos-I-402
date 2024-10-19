package com.ciberfisicos1.trazabilidad.repository.proceso_repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;

@Repository
public interface ProcesoRepository extends JpaRepository<Proceso, Long> {
    default List<Proceso> findProcesoByStatusDecrypted(String status,EncryptionService encryptionService) {
        List<Proceso> procesos = findAll();
        return procesos.stream()
                .filter(proceso -> {
                    String decryptedStatus = encryptionService.decryptData(proceso.getStatus(), 999L);
                    return status.equals(decryptedStatus);
                })
                .collect(Collectors.toList());
    }
    @Query("SELECT p FROM Proceso p ORDER BY p.iniDate DESC LIMIT 1")
    Optional<Proceso> findMostRecentProceso();
    @Query("SELECT p FROM Proceso p WHERE p.iniDate >= :startDate")
    List<Proceso> findProcesosFromDate(@Param("startDate") Date startDate);
    @Query("SELECT p FROM Proceso p WHERE p.iniDate >= :startDate AND p.iniDate < :endDate")
    List<Proceso> findProcesosFromDateRange(@Param("startDate") Date startDate, @Param("endDate")Date endDate);
}