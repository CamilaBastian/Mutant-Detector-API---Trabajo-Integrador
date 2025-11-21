package org.example.mutant_detector.repository;

import org.example.mutant_detector.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Heredar de JpaRepository nos da métodos básicos (save, findById, etc.)
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    // Metodo para buscar por Hash (uso del caché de resultados)
    // Spring genera: SELECT * FROM dna_records WHERE dna_hash = ?
    Optional<DnaRecord> findByDnaHash(String dnaHash);

    // Metodo para contar cuántos registros son mutantes o humanos (para estadísticas)
    // Spring genera: SELECT COUNT(*) FROM dna_records WHERE is_mutant = ?
    long countByIsMutant(boolean isMutant);
}