package org.example.mutant_detector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;

@Data
@Builder // Útil para crear el objeto de forma más limpia en el servicio
public class StatsResponse {

    @JsonProperty("count_mutant_dna") // Nombre de campo que espera la API
    private long countMutantDna;

    @JsonProperty("count_human_dna") // Nombre de campo que espera la API
    private long countHumanDna;

    @JsonProperty("ratio") // Nombre de campo que espera la API
    private double ratio;

}