package com.ciberfisicos1.trazabilidad.repository.tarea_repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ciberfisicos1.trazabilidad.model.tarea.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    @Query("SELECT t FROM Tarea t ORDER BY t.tareaId DESC")
    List<Tarea> findTopNTareas(Pageable pageable);
}
