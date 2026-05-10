package com.example.mscuentasmovimientos.controller;

import com.example.mscuentasmovimientos.dto.MovimientoCreateDto;
import com.example.mscuentasmovimientos.dto.MovimientoDto;
import com.example.mscuentasmovimientos.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<MovimientoDto> createMovimiento(@Valid @RequestBody MovimientoCreateDto movimientoCreateDto) {
        MovimientoDto movimientoDto = movimientoService.createMovimiento(movimientoCreateDto);
        return ResponseEntity.created(URI.create("/api/movimientos/" + movimientoDto.getId()))
                .body(movimientoDto);
    }

    @GetMapping
    public ResponseEntity<List<MovimientoDto>> findAllMovimientos() {
        return ResponseEntity.ok(movimientoService.findAllMovimientos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDto> findMovimientoById(@PathVariable UUID id) {
        return ResponseEntity.ok(movimientoService.findMovimientoById(id));
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<MovimientoDto>> findMovimientosByCuentaId(@PathVariable UUID cuentaId) {
        return ResponseEntity.ok(movimientoService.findMovimientosByCuentaId(cuentaId));
    }

    @GetMapping("/cuenta/{cuentaId}/fecha")
    public ResponseEntity<List<MovimientoDto>> findMovimientosByCuentaIdAndFechaBetween(
            @PathVariable UUID cuentaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(movimientoService.findMovimientosByCuentaIdAndFechaBetween(cuentaId, fechaInicio, fechaFin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable UUID id) {
        movimientoService.deleteMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}