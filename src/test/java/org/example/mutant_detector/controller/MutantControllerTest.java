package org.example.mutant_detector.controller;

import org.example.mutant_detector.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // Configura MockMvc para simular peticiones
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto para simular peticiones HTTP

    @Autowired
    private DnaRecordRepository repository; // Usado para limpiar la BD y verificar el caché

    // JSON base para el test, usando el triple-quote de Java 15+
    private static final String MUTANT_DNA_JSON = """
            {
              "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
            }
            """;

    private static final String HUMAN_DNA_JSON = """
            {
              "dna": ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]
            }
            """;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos H2 antes de cada test para aislar las pruebas
        repository.deleteAll();
    }

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK cuando es mutante")
    void testCheckMutant_returnOk_whenIsMutant() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MUTANT_DNA_JSON))
                .andExpect(status().isOk()); // Espera HTTP 200
    }
    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden cuando es humano")
    void testCheckMutant_returnForbidden_whenIsHuman() throws Exception {
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(HUMAN_DNA_JSON))
                .andExpect(status().isForbidden()); // Espera HTTP 403
    }
    @Test
    @DisplayName("GET /stats debe retornar 0/0.0 inicialmente")
    void testGetStats_returnZerosInitially() throws Exception {
        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verifica que los conteos sean 0 y el ratio sea 0.0
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("\"count_mutant_dna\":0"));
                    assertTrue(responseBody.contains("\"count_human_dna\":0"));
                    assertTrue(responseBody.contains("\"ratio\":0.0"));
                });
    }
    @Test
    @DisplayName("Integración: Probar Mutante, Humano, Caché y Stats finales")
    void testFullIntegrationFlow() throws Exception {
        // 1. Envía Mutante (Debe pasar)
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(MUTANT_DNA_JSON))
                .andExpect(status().isOk());

        // 2. Envía Humano (Debe ser Forbidden)
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(HUMAN_DNA_JSON))
                .andExpect(status().isForbidden());

        // 3. Prueba de Caché: Vuelve a enviar el Mutante.
        // NO debe ejecutar el algoritmo, sino leer de BD. Debe seguir dando 200 OK.
        mockMvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON).content(MUTANT_DNA_JSON))
                .andExpect(status().isOk());

        // 4. Verifica Stats: 1 Mutante, 1 Humano. Ratio 1.0
        mockMvc.perform(get("/stats").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String body = result.getResponse().getContentAsString();
                    assertTrue(body.contains("\"count_mutant_dna\":1"));
                    assertTrue(body.contains("\"count_human_dna\":1"));
                    assertTrue(body.contains("\"ratio\":0.5"));
                });
    }
}