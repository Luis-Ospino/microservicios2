package com.example.mscuentasmovimientos.controller;

import com.example.mscuentasmovimientos.dto.reporte.ReporteDto;
import com.example.mscuentasmovimientos.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reportes", description = "API para generación de reportes bancarios")
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    @Operation(summary = "Generar reporte bancario",
               description = "Genera un reporte completo con información del cliente, sus cuentas y movimientos en un período específico")
    public ResponseEntity<ReporteDto> generarReporte(
            @Parameter(description = "ID del cliente", required = true)
            @RequestParam UUID clienteId,

            @Parameter(description = "Fecha de inicio del período (formato: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,

            @Parameter(description = "Fecha de fin del período (formato: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        log.info("Solicitud de reporte para cliente ID: {}, período: {} - {}", clienteId, fechaInicio, fechaFin);

        // Validar parámetros
        if (fechaInicio.isAfter(fechaFin)) {
            log.warn("Fecha de inicio posterior a fecha de fin: {} > {}", fechaInicio, fechaFin);
            return ResponseEntity.badRequest().build();
        }

        if (fechaInicio.isAfter(LocalDateTime.now())) {
            log.warn("Fecha de inicio futura no permitida: {}", fechaInicio);
            return ResponseEntity.badRequest().build();
        }

        try {
            ReporteDto reporte = reporteService.generarReporte(clienteId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            log.error("Error al generar reporte para cliente ID: {}", clienteId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}