package com.example.msclientespersonas.service;

import com.example.msclientespersonas.dto.PersonaCreateDto;
import com.example.msclientespersonas.dto.PersonaDto;
import com.example.msclientespersonas.entity.Persona;
import com.example.msclientespersonas.exception.ResourceNotFoundException;
import com.example.msclientespersonas.mapper.PersonaMapper;
import com.example.msclientespersonas.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    @Override
    public PersonaDto createPersona(PersonaCreateDto personaCreateDto) {
        if (personaRepository.existsByIdentificacion(personaCreateDto.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe una persona con la identificación: " + personaCreateDto.getIdentificacion());
        }

        Persona persona = personaMapper.toEntity(personaCreateDto);
        Persona personaGuardada = personaRepository.save(persona);
        return personaMapper.toDto(personaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaDto> findAllPersonas() {
        return personaRepository.findAll()
                .stream()
                .map(personaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PersonaDto findPersonaById(UUID id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id=" + id));
        return personaMapper.toDto(persona);
    }

    @Override
    public PersonaDto updatePersona(UUID id, PersonaCreateDto personaCreateDto) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id=" + id));

        // Verificar si la identificación ya existe en otra persona
        if (!persona.getIdentificacion().equals(personaCreateDto.getIdentificacion()) &&
            personaRepository.existsByIdentificacion(personaCreateDto.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe una persona con la identificación: " + personaCreateDto.getIdentificacion());
        }

        Persona personaActualizada = personaMapper.toEntity(personaCreateDto);
        personaActualizada.setId(id);
        personaActualizada.setCreatedAt(persona.getCreatedAt());

        Persona personaGuardada = personaRepository.save(personaActualizada);
        return personaMapper.toDto(personaGuardada);
    }

    @Override
    public void deletePersona(UUID id) {
        if (!personaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Persona no encontrada con id=" + id);
        }
        personaRepository.deleteById(id);
    }
}