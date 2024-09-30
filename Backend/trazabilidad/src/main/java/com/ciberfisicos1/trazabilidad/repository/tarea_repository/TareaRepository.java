package com.ciberfisicos1.trazabilidad.repository.tarea_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
}
