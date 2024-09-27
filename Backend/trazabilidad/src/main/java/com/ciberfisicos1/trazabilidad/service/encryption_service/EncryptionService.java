package com.ciberfisicos1.trazabilidad.service.encryption_service;

import java.security.Security;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import com.ciberfisicos1.trazabilidad.errors.exceptions.InternalServerErrorException;

import jakarta.annotation.PostConstruct;


@Service
public class EncryptionService {

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

    public String encryptData(String data, String masterKey, String version) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(masterKey), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return version + "_" + Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al encriptar los datos");
        }
    }

    public String decryptData(String encryptedData, String masterKey) {
        try {
            String[] parts = encryptedData.split("_", 2);
            String data = parts[1];

            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(masterKey), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(decryptedData);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al desencriptar los datos");
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