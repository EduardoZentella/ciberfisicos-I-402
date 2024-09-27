package com.ciberfisicos1.trazabilidad.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;

@SpringBootTest
public class EncryptionServiceIntegrationTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void whenGenerateMasterKey_thenKeyIsGenerated() {
        // when
        String masterKey = encryptionService.generateMasterKey();

        // then
        assertThat(masterKey).isNotNull();
        System.out.println("Prueba de integración de EncryptionService exitosa: Clave maestra generada correctamente.");
    }

    @Test
    public void whenEncryptAndDecryptMasterKey_thenDecryptionIsValid() {
        // given
        String masterKey = encryptionService.generateMasterKey();
        String password = "password";

        // when
        String encryptedMasterKey = encryptionService.encryptMasterKey(masterKey, password);
        String decryptedMasterKey = encryptionService.decryptMasterKey(encryptedMasterKey, password);

        // then
        assertThat(decryptedMasterKey).isEqualTo(masterKey);
        System.out.println("Prueba de integración de EncryptionService exitosa: Clave maestra encriptada y desencriptada correctamente.");
    }

    @Test
    public void whenEncryptAndDecryptData_thenDecryptionIsValid() {
        // given
        String masterKey = encryptionService.generateMasterKey();
        String data = "Sensitive Data";
        String version = "V1";

        // when
        String encryptedData = encryptionService.encryptData(data, masterKey, version);
        String decryptedData = encryptionService.decryptData(encryptedData, masterKey);

        // then
        assertThat(decryptedData).isEqualTo(data);
        System.out.println("Prueba de integración de EncryptionService exitosa: Datos encriptados y desencriptados correctamente.");
    }
}
