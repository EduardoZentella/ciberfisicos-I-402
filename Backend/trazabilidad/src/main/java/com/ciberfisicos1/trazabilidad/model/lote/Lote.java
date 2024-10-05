package com.ciberfisicos1.trazabilidad.model.lote;

import com.ciberfisicos1.trazabilidad.model.proceso.Proceso;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import com.ciberfisicos1.trazabilidad.model.dto.LoteDTO;
import com.ciberfisicos1.trazabilidad.model.pieza.Pieza;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Lote_Id")
    private Long loteId;

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonBackReference
    private List<Pieza> piezas = new ArrayList<>();

    @ManyToOne
    @Nonnull
    @JoinColumn(name = "Proceso_Id", referencedColumnName = "Proceso_Id", nullable = false)
    private Proceso proceso;

    public LoteDTO toDTO() {
        LoteDTO dto = new LoteDTO();
        dto.setLoteId(this.loteId);
        dto.setProcesoId(this.proceso.getProcesoId());
        return dto;
    }
    
    // Getters y Setters
}
