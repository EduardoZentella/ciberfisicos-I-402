package com.ciberfisicos1.trazabilidad.model.dto;

import lombok.Data;

@Data
public class PiezaDTO {
    private Long piezaId;
    private String type;
    private Long loteId;
}
