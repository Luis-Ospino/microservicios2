package com.example.mscuentasmovimientos.controller;

import com.example.mscuentasmovimientos.entity.Cuenta;
import com.example.mscuentasmovimientos.entity.enums.EstadoCuenta;
import com.example.mscuentasmovimientos.entity.enums.TipoCuenta;
import com.example.mscuentasmovimientos.repository.CuentaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovimientoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CuentaRepository cuentaRepository;

    @BeforeEach
    void setUp() {
        cuentaRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/movimientos crea un movimiento exitosamente y retorna 201 con response JSON")
    void postMovimiento_whenRequestIsValid_thenReturnsCreatedAndJsonResponse() throws Exception {
        UUID cuentaId = UUID.randomUUID();
        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta("1234567890")
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldoInicial(new BigDecimal("500.00"))
                .saldoDisponible(new BigDecimal("500.00"))
                .estado(EstadoCuenta.ACTIVA)
                .clienteId(UUID.randomUUID())
                .build();
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);

        Map<String, Object> requestBody = Map.of(
                "fecha", LocalDateTime.now().withNano(0).toString(),
                "tipoMovimiento", "RETIRO",
                "valor", -150.00,
                "cuentaId", cuentaGuardada.getId().toString()
        );

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.containsString("/api/movimientos/")))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tipoMovimiento").value("RETIRO"))
                .andExpect(jsonPath("$.valor").value(-150.0))
                .andExpect(jsonPath("$.saldo").value(350.0))
                .andExpect(jsonPath("$.cuentaId").value(cuentaGuardada.getId().toString()));
    }
}
