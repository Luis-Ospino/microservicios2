package com.example.msclientespersonas.mapper;

import com.example.msclientespersonas.dto.ClienteCreateDto;
import com.example.msclientespersonas.dto.ClienteDto;
import com.example.msclientespersonas.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDto toDto(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", constant = "true")
    @Mapping(target = "fechaCreacion", expression = "java(java.time.LocalDateTime.now())")
    Cliente toEntity(ClienteCreateDto clienteCreateDto);
}