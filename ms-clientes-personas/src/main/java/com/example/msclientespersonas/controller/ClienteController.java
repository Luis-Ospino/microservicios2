package com.example.msclientespersonas.controller;

import com.example.msclientespersonas.dto.ClienteCreateDto;
import com.example.msclientespersonas.dto.ClienteDto;
import com.example.msclientespersonas.dto.ClienteUpdateDto;
import com.example.msclientespersonas.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDto> createCliente(@Valid @RequestBody ClienteCreateDto clienteCreateDto) {
        ClienteDto clienteDto = clienteService.createCliente(clienteCreateDto);
        return ResponseEntity.created(URI.create("/api/clientes/" + clienteDto.getId()))
                .body(clienteDto);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> findAllClientes() {
        return ResponseEntity.ok(clienteService.findAllClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> findClienteById(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteService.findClienteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> updateCliente(@PathVariable UUID id, @Valid @RequestBody ClienteUpdateDto clienteUpdateDto) {
        return ResponseEntity.ok(clienteService.updateCliente(id, clienteUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable UUID id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}