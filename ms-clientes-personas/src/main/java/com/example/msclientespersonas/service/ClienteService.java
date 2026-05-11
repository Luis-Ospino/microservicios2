package com.example.msclientespersonas.service;

import com.example.msclientespersonas.dto.ClienteCreateDto;
import com.example.msclientespersonas.dto.ClienteDto;
import com.example.msclientespersonas.dto.ClienteUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ClienteService {

    ClienteDto createCliente(ClienteCreateDto clienteCreateDto);

    List<ClienteDto> findAllClientes();

    ClienteDto findClienteById(UUID id);

    ClienteDto updateCliente(UUID id, ClienteUpdateDto clienteUpdateDto);

    void deleteCliente(UUID id);
}