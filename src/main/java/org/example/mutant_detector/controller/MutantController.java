package org.example.mutant_detector.controller;

import org.example.mutant_detector.dto.DnaRequest;
import org.example.mutant_detector.dto.StatsResponse;
import org.example.mutant_detector.service.MutantService;
import org.example.mutant_detector.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Detector de Mutantes", description = "Endpoints para el análisis de ADN y estadísticas.") // Anotación de clase
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    // Endpoint 1: POST /mutant
    @Operation(summary = "Analiza una secuencia de ADN para determinar si es mutante.") // Documentación
    @ApiResponse(responseCode = "200", description = "Es Mutante (pasa la verificación).") // Documenta el 200
    @ApiResponse(responseCode = "403", description = "No es Mutante (es Humano).") // Documenta el 403
    @ApiResponse(responseCode = "400", description = "Solicitud inválida (ADN no cuadrado o caracteres incorrectos).") // Documenta la validación
    @PostMapping("/mutant")
    public ResponseEntity<Void> isMutant(@Valid @RequestBody DnaRequest request) {

        boolean isMutant = mutantService.isMutant(request.getDna());

        if (isMutant) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint 2: GET /stats
    @Operation(summary = "Retorna las estadísticas de los análisis de ADN realizados.") // Documentación
    @ApiResponse(responseCode = "200", description = "Estadísticas devueltas con éxito.") // Documenta el 200
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}