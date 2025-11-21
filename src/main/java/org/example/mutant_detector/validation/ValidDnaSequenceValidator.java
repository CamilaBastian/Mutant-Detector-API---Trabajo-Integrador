package org.example.mutant_detector.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final int MIN_SIZE = 4;
    // Patrón para verificar que la fila solo contiene A, T, C, G
    private static final Pattern VALID_CHARS_PATTERN = Pattern.compile("^[ATCG]+$");

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length < MIN_SIZE) {
            return false; // Matriz nula o muy pequeña
        }

        final int n = dna.length;

        for (String row : dna) {
            // Regla 3 & 4: Debe ser NxN y solo caracteres válidos
            if (row == null || row.length() != n || !VALID_CHARS_PATTERN.matcher(row).matches()) {
                return false; // Fila nula, no cuadrada o caracteres inválidos
            }
        }

        return true; // Pasa todas las validaciones estructurales
    }
}