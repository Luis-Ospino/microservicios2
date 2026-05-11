package com.example.msclientespersonas.service;

import com.example.msclientespersonas.dto.ClienteCreateDto;
import com.example.msclientespersonas.dto.ClienteCreadoEvent;
import com.example.msclientespersonas.dto.ClienteDto;
import com.example.msclientespersonas.dto.ClienteUpdateDto;
import com.example.msclientespersonas.entity.Cliente;
import com.example.msclientespersonas.exception.ResourceNotFoundException;
import com.example.msclientespersonas.mapper.ClienteMapper;
import com.example.msclientespersonas.messaging.ClientePublisher;
import com.example.msclientespersonas.repository.ClienteRepository;
import com.example.msclientespersonas.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository;
    private final ClienteMapper clienteMapper;
    private final ClientePublisher clientePublisher;

    @Override
    public ClienteDto createCliente(ClienteCreateDto clienteCreateDto) {
        // Verificar que la persona existe
        if (!personaRepository.existsById(clienteCreateDto.getPersonaId())) {
            throw new IllegalArgumentException("Persona no encontrada con id=" + clienteCreateDto.getPersonaId());
        }

        // Verificar que no exista ya un cliente para esta persona
        if (clienteRepository.existsByPersonaId(clienteCreateDto.getPersonaId())) {
            throw new IllegalArgumentException("Ya existe un cliente para la persona con id=" + clienteCreateDto.getPersonaId());
        }

        Cliente cliente = clienteMapper.toEntity(clienteCreateDto);
        Cliente clienteGuardado = clienteRepository.save(cliente);

        // Publicar evento de cliente creado
        try {
            ClienteCreadoEvent event = ClienteCreadoEvent.builder()
                    .clienteId(clienteGuardado.getId())
                    .personaId(clienteGuardado.getPersonaId())
                    .contrasena(clienteGuardado.getContrasena())
                    .fechaCreacion(clienteGuardado.getCreatedAt())
                    .build();
            clientePublisher.publicarClienteCreado(event);
        } catch (Exception e) {
            log.error("Error al publicar evento cliente.creado para cliente ID: {}", clienteGuardado.getId(), e);
            // No lanzamos excepción para no fallar la creación del cliente
        }

        return clienteMapper.toDto(clienteGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDto> findAllClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDto findClienteById(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id=" + id));
        return clienteMapper.toDto(cliente);
    }

    @Override
    public ClienteDto updateCliente(UUID id, ClienteUpdateDto clienteUpdateDto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id=" + id));

        clienteMapper.updateEntityFromDto(clienteUpdateDto, cliente);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteGuardado);
    }

    @Override
    public void deleteCliente(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id=" + id);
        }
        clienteRepository.deleteById(id);
    }
}