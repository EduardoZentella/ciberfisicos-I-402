package com.ciberfisicos1.trazabilidad.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TareaDTO {
    private Long tareaId;
    private String name;
    private String description;
    private Date iniDate;
    private Date endDate;
    private String status;
    private Long procesoId;
}