package com.ciberfisicos1.trazabilidad.repository.pieza_repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.pieza.Pieza;

@Repository

public interface PiezaRepository extends JpaRepository<Pieza, Long> {
    List<Pieza> findByLoteId(Long loteId);
}
