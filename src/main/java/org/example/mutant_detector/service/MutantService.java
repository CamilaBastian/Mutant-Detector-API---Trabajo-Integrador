package org.example.mutant_detector.service;

import org.example.mutant_detector.entity.DnaRecord;
import org.example.mutant_detector.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.security.MessageDigest;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok genera constructor con inyección de dependencias
public class MutantService {

    private final MutantDetector detector;
    private final DnaRecordRepository repository;

    /**
     * Verifica si un ADN es mutante, aplicando caché y persistencia.
     */
    public boolean isMutant(String[] dna) {
        // 1. Unir el array de strings en un solo string para calcular el hash
        String fullDnaSequence = String.join("", dna);
        String dnaHash = calculateSha256Hash(fullDnaSequence);

        // 2. Optimización 2: Buscar en la BD (Caché de resultados)
        Optional<DnaRecord> cachedRecord = repository.findByDnaHash(dnaHash);
        if (cachedRecord.isPresent()) {
            return cachedRecord.get().getIsMutant(); // Retorna resultado cacheado
        }

        // 3. Si no existe, ejecutar el algoritmo
        boolean isMutant = detector.isMutant(dna);

        // 4. Guardar el resultado en la BD para futuro caché
        DnaRecord newRecord = new DnaRecord(dnaHash, isMutant);
        repository.save(newRecord);

        return isMutant;
    }

    /**
     * Calcula el hash SHA-256 de una cadena de texto.
     */
    private String calculateSha256Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());

            // Convierte el array de bytes a una cadena hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            // Manejo de errores simple; en producción usaríamos una excepción custom.
            throw new RuntimeException("Error al calcular el hash SHA-256", e);
        }
    }
}