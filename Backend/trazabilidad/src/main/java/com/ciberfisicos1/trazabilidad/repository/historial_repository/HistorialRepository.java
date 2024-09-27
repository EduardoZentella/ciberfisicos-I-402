package com.ciberfisicos1.trazabilidad.repository.historial_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ciberfisicos1.trazabilidad.model.historial.Historial;
import com.ciberfisicos1.trazabilidad.model.historial.HistorialId;
import java.util.List;
import java.util.Optional;


@Repository
public interface HistorialRepository extends JpaRepository<Historial, HistorialId> {
    List<Historial> findByHistorialIdUsuarioId(Long usuarioId);
    List<Historial> findByVersion(String version);
    Historial findByHistorialIdUsuarioIdAndVersion(Long usuarioId, String version);
    @Query("SELECT COALESCE(MAX(h.historialId.id), 0) FROM Historial h WHERE h.historialId.usuarioId = :usuarioId")
    Optional<Long> findMaxIdByUsuarioId(@Param("usuarioId") Long usuarioId);
}
