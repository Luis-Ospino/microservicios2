package com.example.mscuentasmovimientos.mapper;

import com.example.mscuentasmovimientos.dto.MovimientoCreateDto;
import com.example.mscuentasmovimientos.dto.MovimientoDto;
import com.example.mscuentasmovimientos.entity.Movimiento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    MovimientoDto toDto(Movimiento movimiento);

    Movimiento toEntity(MovimientoCreateDto movimientoCreateDto);
}