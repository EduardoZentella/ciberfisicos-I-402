package com.ciberfisicos1.trazabilidad.service.cache_service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.ciberfisicos1.trazabilidad.errors.exceptions.InternalServerErrorException;
import com.ciberfisicos1.trazabilidad.model.historial.Historial;
import com.ciberfisicos1.trazabilidad.repository.historial_repository.HistorialRepository;
import com.ciberfisicos1.trazabilidad.service.encryption_service.EncryptionService;

@Service
public class CacheService {

    private final Map<Long, CacheEntry> cache = new ConcurrentHashMap<>();

    public CacheEntry getUserCache(Long usuarioId) {
        return cache.computeIfAbsent(usuarioId, k -> new CacheEntry());
    }

    public String getMasterKey(Long usuarioId, String version, HistorialRepository historialRepository, EncryptionService encryptionService) {
        CacheEntry cacheEntry = cache.computeIfAbsent(usuarioId, k -> new CacheEntry());
        return cacheEntry.getMasterKey(version, () -> {
            Optional<Historial> historial = Optional.of(historialRepository.findByHistorialIdUsuarioIdAndVersion(usuarioId, version));
            if (historial.isPresent()) {
                return encryptionService.decryptMasterKey(historial.get().getMasterKey(), historial.get().getContraseña());
            } else {
                throw new InternalServerErrorException("No se encontró el historial correspondiente a la versión");
            }
        });
    }

    public static class CacheEntry {
        private final Map<String, String> versionToMasterKey = new ConcurrentHashMap<>();
        private String latestVersion;
        private String latestMasterKey;

        public String getMasterKey(String version, Supplier<String> masterKeySupplier) {
            return versionToMasterKey.computeIfAbsent(version, v -> {
                String masterKey = masterKeySupplier.get();
                if (latestVersion == null || latestVersion.compareTo(version) < 0) {
                    latestVersion = version;
                    latestMasterKey = masterKey;
                }
                return masterKey;
            });
        }

        public String getLatestVersion() {
            return latestVersion;
        }

        public String getLatestMasterKey() {
            return latestMasterKey;
        }
    }
}
