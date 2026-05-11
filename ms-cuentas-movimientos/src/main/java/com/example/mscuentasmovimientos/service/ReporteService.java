package com.example.mscuentasmovimientos.service;

import com.example.mscuentasmovimientos.dto.reporte.*;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ReporteService {

    ReporteDto generarReporte(UUID clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}