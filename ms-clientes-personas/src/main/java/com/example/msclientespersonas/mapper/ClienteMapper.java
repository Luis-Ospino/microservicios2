package com.example.msclientespersonas.mapper;

import com.example.msclientespersonas.dto.ClienteCreateDto;
import com.example.msclientespersonas.dto.ClienteDto;
import com.example.msclientespersonas.entity.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDto toDto(Cliente cliente);

    Cliente toEntity(ClienteCreateDto clienteCreateDto);
}