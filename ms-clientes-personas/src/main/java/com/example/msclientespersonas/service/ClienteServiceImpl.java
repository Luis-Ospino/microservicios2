package com.example.msclientespersonas.service;

import com.example.msclientespersonas.dto.ClienteCreateDto;
import com.example.msclientespersonas.dto.ClienteDto;
import com.example.msclientespersonas.entity.Cliente;
import com.example.msclientespersonas.exception.ResourceNotFoundException;
import com.example.msclientespersonas.mapper.ClienteMapper;
import com.example.msclientespersonas.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public ClienteDto createCliente(ClienteCreateDto clienteCreateDto) {
        Cliente cliente = clienteMapper.toEntity(clienteCreateDto);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteGuardado);
    }

    @Override
    public List<ClienteDto> findAllClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDto findClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id=" + id));
        return clienteMapper.toDto(cliente);
    }
}