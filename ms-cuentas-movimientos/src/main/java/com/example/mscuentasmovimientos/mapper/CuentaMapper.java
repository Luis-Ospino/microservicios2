package com.example.mscuentasmovimientos.mapper;

import com.example.mscuentasmovimientos.dto.CuentaCreateDto;
import com.example.mscuentasmovimientos.dto.CuentaDto;
import com.example.mscuentasmovimientos.entity.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

    CuentaDto toDto(Cuenta cuenta);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "true")
    @Mapping(target = "fechaCreacion", expression = "java(java.time.LocalDateTime.now())")
    Cuenta toEntity(CuentaCreateDto cuentaCreateDto);
}