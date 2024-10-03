package com.ciberfisicos1.trazabilidad.model.dto;

import lombok.Data;

@Data
public class TareaDTO {
    private Long tareaId;
    private String name;
    private String description;
    private Long procesoId;
}