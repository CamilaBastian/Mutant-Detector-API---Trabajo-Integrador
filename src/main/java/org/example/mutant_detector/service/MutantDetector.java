package org.example.mutant_detector.service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private char[][] matrix;
    private int N;

    /**
     * Determina si la secuencia de ADN corresponde a un mutante.
     * @param dna Matriz de Strings que representa el ADN.
     * @return true si es mutante (más de una secuencia de 4 letras iguales), false en caso contrario.
     */
    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length == 0) {
            return false;
        }
        N = dna.length;
        matrix = new char[N][N];
        for (int i = 0; i < N; i++) {
            matrix[i] = dna[i].toCharArray();
        }
        boolean[][] visited = new boolean[N][N]; // Matriz para rastrear posiciones visitadas
        int sequenceCount = 0;

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                // 1. Horizontal (→)
                if (col <= N - SEQUENCE_LENGTH && !visited[row][col]) {
                    if (checkSequence(row, col, 0, 1, visited)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                        // Marcar las posiciones de la secuencia como visitadas
                        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
                            visited[row][col + i] = true;
                        }
                    }
                }
                // 2. Vertical (↓)
                if (row <= N - SEQUENCE_LENGTH && !visited[row][col]) {
                    if (checkSequence(row, col, 1, 0, visited)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                        // Marcar las posiciones de la secuencia como visitadas
                        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
                            visited[row + i][col] = true;
                        }
                    }
                }
                // 3. Diagonal Descendente (↘)
                if (row <= N - SEQUENCE_LENGTH && col <= N - SEQUENCE_LENGTH && !visited[row][col]) {
                    if (checkSequence(row, col, 1, 1, visited)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                        // Marcar las posiciones de la secuencia como visitadas
                        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
                            visited[row + i][col + i] = true;
                        }
                    }
                }
                // 4. Diagonal Ascendente (↗)
                if (row >= SEQUENCE_LENGTH - 1 && col <= N - SEQUENCE_LENGTH && !visited[row][col]) {
                    if (checkSequence(row, col, -1, 1, visited)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                        // Marcar las posiciones de la secuencia como visitadas
                        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
                            visited[row - i][col + i] = true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Chequea si hay una secuencia de 4 letras iguales a partir de (startRow, startCol)
     * en la dirección dada por (rowDirection, colDirection), evitando posiciones ya visitadas.
     */
    private boolean checkSequence(int startRow, int startCol, int rowDirection, int colDirection, boolean[][] visited) {
        final char base = matrix[startRow][startCol];
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            int currentRow = startRow + i * rowDirection;
            int currentCol = startCol + i * colDirection;
            if (visited[currentRow][currentCol] || matrix[currentRow][currentCol] != base) {
                return false;
            }
        }
        return true;
    }

}