package com.example.msclientespersonas.service;

import com.example.msclientespersonas.dto.ClienteCreateDto;
import com.example.msclientespersonas.dto.ClienteDto;

import java.util.List;

public interface ClienteService {

    ClienteDto createCliente(ClienteCreateDto clienteCreateDto);

    List<ClienteDto> findAllClientes();

    ClienteDto findClienteById(Long id);
}