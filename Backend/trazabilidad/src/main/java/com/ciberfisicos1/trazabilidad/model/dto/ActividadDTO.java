package com.ciberfisicos1.trazabilidad.model.dto;

import lombok.Data;

@Data
public class ActividadDTO {
    private Long actividadId;
    private String name;
    private String description;
    private Long tareaId;
}
