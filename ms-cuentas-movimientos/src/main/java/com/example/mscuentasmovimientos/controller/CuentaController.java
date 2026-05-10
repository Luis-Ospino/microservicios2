package com.example.mscuentasmovimientos.controller;

import com.example.mscuentasmovimientos.dto.CuentaCreateDto;
import com.example.mscuentasmovimientos.dto.CuentaDto;
import com.example.mscuentasmovimientos.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaDto> createCuenta(@Valid @RequestBody CuentaCreateDto cuentaCreateDto) {
        CuentaDto cuentaDto = cuentaService.createCuenta(cuentaCreateDto);
        return ResponseEntity.created(URI.create("/api/cuentas/" + cuentaDto.getId()))
                .body(cuentaDto);
    }

    @GetMapping
    public ResponseEntity<List<CuentaDto>> findAllCuentas() {
        return ResponseEntity.ok(cuentaService.findAllCuentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDto> findCuentaById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findCuentaById(id));
    }
}