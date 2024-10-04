package com.ciberfisicos1.trazabilidad.model.pieza;


import com.ciberfisicos1.trazabilidad.model.dto.PiezaDTO;
import com.ciberfisicos1.trazabilidad.model.lote.Lote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pieza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piezaId;

    private Long loteId;
    private String type;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "Lote_Id", referencedColumnName = "Lote_Id", nullable = false)
    private Lote lote;

    public PiezaDTO toDTO() {
        PiezaDTO dto = new PiezaDTO();
        dto.setPiezaId(this.piezaId);
        dto.setLoteId(this.lote.getLoteId());
        return dto;
    }

    // Getters y Setters
}
