package com.ciberfisicos1.trazabilidad.repository.proceso_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;

@Repository
public interface ProcesoRepository extends JpaRepository<Proceso, Long> {
}