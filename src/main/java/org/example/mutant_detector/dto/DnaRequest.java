package org.example.mutant_detector.dto;

import lombok.Data;
import org.example.mutant_detector.validation.ValidDnaSequence; // Importación de la anotación

@Data // Proporciona getters, setters, toString, etc., gracias a Lombok
public class DnaRequest {

    // 💡 Validación 5: La anotación personalizada asegura que el ADN sea NxN y válido
    @ValidDnaSequence
    private String[] dna;

}