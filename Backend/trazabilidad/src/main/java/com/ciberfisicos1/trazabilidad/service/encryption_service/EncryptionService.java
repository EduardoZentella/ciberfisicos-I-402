package com.ciberfisicos1.trazabilidad.service.encryption_service;

import java.security.Security;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import com.ciberfisicos1.trazabilidad.errors.exceptions.InternalServerErrorException;
import com.ciberfisicos1.trazabilidad.model.historial.Historial;
import com.ciberfisicos1.trazabilidad.repository.historial_repository.HistorialRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EncryptionService {

    private final HistorialRepository historialRepository;
    
    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String generateMasterKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al generar la clave maestra");
        }
    }

    public String encryptMasterKey(String masterKey, String password) {
        try {
            byte[] key = deriveKey(password);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedKey = cipher.doFinal(masterKey.getBytes());
            return Base64.getEncoder().encodeToString(encryptedKey);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al encriptar la clave maestra");
        }
    }

    public String decryptMasterKey(String encryptedMasterKey, String password) {
        try {
            byte[] key = deriveKey(password);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedKey = cipher.doFinal(Base64.getDecoder().decode(encryptedMasterKey));
            return new String(decryptedKey);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al desencriptar la clave maestra");
        }
    }

    public String encryptData(String data, Long usuarioId) {
        // Obtener la versión más reciente del historial
        Optional<Historial> latestHistorial = historialRepository.findByHistorialIdUsuarioId(usuarioId).stream().findFirst();
        if (latestHistorial.isPresent()) {
            String masterKey = decryptMasterKey(latestHistorial.get().getMasterKey(), latestHistorial.get().getContraseña());
            try {
                byte[] decodedKey = Base64.getDecoder().decode(masterKey);
                if (decodedKey.length != 32) {
                    throw new InternalServerErrorException("La longitud de la clave maestra no es válida");
                }
                SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "AES");
    
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                byte[] encryptedData = cipher.doFinal(data.getBytes());
                return latestHistorial.get().getVersion() + "_" + Base64.getEncoder().encodeToString(encryptedData);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerErrorException("Error al encriptar los datos");
            }
        } else {
            throw new InternalServerErrorException("No se encontró el historial para el usuario");
        }
    }

    public String decryptData(String encryptedData, Long usuarioId) {
        // Obtener la versión de los datos encriptados
        String[] parts = encryptedData.split("_", 2);
        String version = parts[0];
        String data = parts[1];

        // Obtener el historial correspondiente a la versión
        Optional<Historial> historial = Optional.of(historialRepository.findByHistorialIdUsuarioIdAndVersion(usuarioId, version));
        if (historial.isPresent()) {
            // Encontrar la contraseña usada para encriptar los datos
            String encryptedPassword = historial.get().getContraseña();

            // Desencriptar la MasterKey con la contraseña usada
            String masterKey = decryptMasterKey(historial.get().getMasterKey(), encryptedPassword);

            // Desencriptar los datos con la MasterKey desencriptada
            try {
                byte[] decodedKey = Base64.getDecoder().decode(masterKey);
                if (decodedKey.length != 32) {
                    throw new InternalServerErrorException("La longitud de la clave maestra no es válida");
                }
                SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "AES");
    
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(data));
                return new String(decryptedData);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerErrorException("Error al desencriptar los datos");
            }
        } else {
            throw new InternalServerErrorException("No se encontró el historial correspondiente a la versión");
        }
    }

    private byte[] deriveKey(String password) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[16], 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            return tmp.getEncoded();
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al derivar la clave");
        }
    }
}