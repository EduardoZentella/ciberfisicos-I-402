package com.ciberfisicos1.trazabilidad.repository.lote_repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.lote.Lote;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByProceso_ProcesoId(Long procesoId);
    
}
