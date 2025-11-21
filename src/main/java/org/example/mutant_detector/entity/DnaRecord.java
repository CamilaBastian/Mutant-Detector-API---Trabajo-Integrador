package org.example.mutant_detector.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data // Proporciona getters, setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dna_records", indexes = {
        // Índices para búsquedas rápidas (Optimización 3)
        @Index(name = "idx_dna_hash", columnList = "dnaHash", unique = true),
        @Index(name = "idx_is_mutant", columnList = "isMutant")
})
public class DnaRecord {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hash SHA-256 del ADN, único y de tamaño fijo
    @Column(name = "dna_hash", nullable = false, length = 64)
    private String dnaHash;

    // true si es mutante, false si es humano
    @Column(name = "is_mutant", nullable = false)
    private Boolean isMutant;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructor usado por el servicio para guardar un nuevo registro
    public DnaRecord(String dnaHash, Boolean isMutant) {
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
        this.createdAt = LocalDateTime.now();
    }
}