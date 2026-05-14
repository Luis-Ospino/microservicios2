package com.example.msclientespersonas.controller;

import com.example.msclientespersonas.dto.ClienteDto;
import com.example.msclientespersonas.entity.enums.EstadoCliente;
import com.example.msclientespersonas.service.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Test
    @DisplayName("GET /api/clientes retorna 200 y una lista de clientes")
    void findAllClientes_whenDataExists_thenReturnsOkAndJsonList() throws Exception {
        UUID clienteId = UUID.randomUUID();
        UUID personaId = UUID.randomUUID();

        ClienteDto clienteDto = ClienteDto.builder()
                .id(clienteId)
                .personaId(personaId)
                .contrasena("supersecret")
                .estado(EstadoCliente.ACTIVO)
                .build();

        when(clienteService.findAllClientes()).thenReturn(List.of(clienteDto));

        mockMvc.perform(get("/api/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(clienteId.toString()))
                .andExpect(jsonPath("$[0].personaId").value(personaId.toString()))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }
}
