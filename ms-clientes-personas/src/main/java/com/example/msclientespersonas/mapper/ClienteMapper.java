package com.example.msclientespersonas.mapper;

import com.example.msclientespersonas.dto.ClienteCreateDto;
import com.example.msclientespersonas.dto.ClienteDto;
import com.example.msclientespersonas.dto.ClienteUpdateDto;
import com.example.msclientespersonas.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDto toDto(Cliente cliente);

    Cliente toEntity(ClienteCreateDto clienteCreateDto);

    void updateEntityFromDto(ClienteUpdateDto dto, @MappingTarget Cliente entity);
}