package com.ciberfisicos1.trazabilidad.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void whenContextLoads_thenDataSourceIsNotNull() {
        assertThat(dataSource).isNotNull();
        System.out.println("Conexión a la base de datos establecida exitosamente.");
    }
}