package com.ciberfisicos1.trazabilidad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;

@SpringBootApplication
public class TrazabilidadApplication {

	public static void main(String[] args) {

		String logPath = System.getProperty("user.dir") + "/src/main/resources/logs";
        File logDir = new File(logPath);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
		SpringApplication.run(TrazabilidadApplication.class, args);
	}
}
