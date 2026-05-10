package com.example.mscuentasmovimientos.mapper;

import com.example.mscuentasmovimientos.dto.MovimientoCreateDto;
import com.example.mscuentasmovimientos.dto.MovimientoDto;
import com.example.mscuentasmovimientos.entity.Movimiento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    MovimientoDto toDto(Movimiento movimiento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Movimiento toEntity(MovimientoCreateDto movimientoCreateDto);
}