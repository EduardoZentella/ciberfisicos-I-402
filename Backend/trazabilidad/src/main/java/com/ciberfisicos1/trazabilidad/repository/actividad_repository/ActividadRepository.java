package com.ciberfisicos1.trazabilidad.repository.actividad_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.actividad.Actividad;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
}

