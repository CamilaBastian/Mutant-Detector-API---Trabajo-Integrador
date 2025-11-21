package org.example.mutant_detector.service;

import org.example.mutant_detector.dto.StatsResponse;
import org.example.mutant_detector.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Inyecta el Repository de forma automática
public class StatsService {

    private final DnaRecordRepository repository;

    /**
     * Calcula las estadísticas de la base de datos (mutantes, humanos y ratio).
     * @return DTO con el conteo de mutantes, humanos y el ratio.
     */
    public StatsResponse getStats() {

        // 1. Obtener los conteos de la BD
        // Gracias a JpaRepository y al metodo personalizado countByIsMutant,
        // esto es una consulta directa a la BD.
        long mutantCount = repository.countByIsMutant(true);
        long humanCount = repository.countByIsMutant(false);

        // 2. Calcular el ratio (con manejo de división por cero)
        double ratio = 0.0;
        long totalCount = mutantCount + humanCount;

        if (totalCount > 0) {
            // Se usa (double) para asegurar la división de punto flotante
            ratio = (double) mutantCount / totalCount;
        }

        // 3. Devolver la respuesta en el formato StatsResponse
        return StatsResponse.builder()
                .countMutantDna(mutantCount)
                .countHumanDna(humanCount)
                .ratio(ratio)
                .build();
    }
}