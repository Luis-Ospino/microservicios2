package com.example.mscuentasmovimientos.mapper;

import com.example.mscuentasmovimientos.dto.CuentaCreateDto;
import com.example.mscuentasmovimientos.dto.CuentaDto;
import com.example.mscuentasmovimientos.dto.CuentaUpdateDto;
import com.example.mscuentasmovimientos.entity.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

    CuentaDto toDto(Cuenta cuenta);

    Cuenta toEntity(CuentaCreateDto cuentaCreateDto);

    void updateEntityFromDto(CuentaUpdateDto dto, @MappingTarget Cuenta entity);
}