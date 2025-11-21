package org.example.mutant_detector.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// No se necesita @SpringBootTest, es un test unitario puro
class MutantDetectorTest {

    private MutantDetector detector;

    @BeforeEach
    void setUp() {
        // Inicializar el detector antes de cada prueba
        detector = new MutantDetector();
    }

    @Test
    @DisplayName("Mutante: 2 secuencias horizontales distintas")
    void testMutantWithTwoHorizontalSequences() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TATTTT", // <-- Una TTTT horizontal
                "AGAAAA", // <-- Una AAAA horizontal
                "CGCCTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutante: Secuencia horizontal y secuencia vertical")
    void testMutantHorizontalAndVertical() {
        String[] dna = {
                "AAAAGC", // <-- Horizontal: AAAA
                "TCGTGC",
                "TCGAAA",
                "TCGAGC",
                "TCGCCT", // <-- Verticales TTTT Fila 1 Columna 0 (T-T-T-T)
                "TCACTG"
        };
        String[] dnaMutant = {
                "AAAACT", // 1. Horizontal
                "GCTTAC",
                "GCTTAC", // 2. Vertical TT en col 3, y T-T-T-T en Col 1 (T, T, T, T)
                "GCTTAC",
                "GCTTAC",
                "GCACTG"
        };
        assertTrue(detector.isMutant(dnaMutant));
    }

    @Test
    @DisplayName("Mutante: Diagonal descendente y diagonal ascendente")
    void testMutantBothDiagonals() {
        String[] dna = {
                "GTACGT",
                "CATTCTC",
                "TTATGT",
                "CTTGAG",
                "CTTCGA",
                "GTATCC"

        };
        String[] dnaMutant = {
                "ATGCGA",
                "CAGTCG",
                "TTACCA", // D: A-A-A-A (diag descendente)
                "AGCACT",
                "TCGCAC", // A: C-C-C-C (diag ascendente)
                "CCACTC"
        };
        assertTrue(detector.isMutant(dnaMutant));
    }

    @Test
    @DisplayName("Mutante: Matriz pequeña 4x4 con 2 secuencias")
    void testMutantSmall4x4() {
        String[] dna = {
                "ATGC",
                "ATGC",
                "ATGC",
                "ATGC" // Vertical AAAA, TTTT, GGGG, CCCC = ¡4 secuencias!
        };
        assertTrue(detector.isMutant(dna));
    }
    @Test
    @DisplayName("Humano: Ninguna secuencia encontrada")
    void testHumanNoSequences() {
        String[] dna = {
                "ATGC",
                "TCAA",
                "GATT",
                "CCTA"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Humano: Solo una secuencia (Horizontal)")
    void testHumanOneSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAAA", // <-- SÓLO UNA secuencia horizontal
                "CGCCTA",
                "TCACTG"
        };
        assertFalse(detector.isMutant(dna));
    }
    @Test
    @DisplayName("Inválido: Matriz demasiado pequeña (3x3)")
    void testInvalidTooSmall() {
        String[] dna = {
                "ATC",
                "ATC",
                "ATC"
        };
        // La validación en el detector fallaría al buscar N-4 (0-4=-4)
        // Pero dado que la validación inicial se encarga de esto (MIN_SIZE=4),
        // este test es importante si el detector es usado sin el validador.
        assertFalse(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Inválido: DNA Nulo")
    void testInvalidNullDna() {
        assertFalse(detector.isMutant(null));
    }
}
