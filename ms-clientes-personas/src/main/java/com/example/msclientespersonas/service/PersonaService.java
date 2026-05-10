package com.example.msclientespersonas.service;

import com.example.msclientespersonas.dto.PersonaCreateDto;
import com.example.msclientespersonas.dto.PersonaDto;

import java.util.List;
import java.util.UUID;

public interface PersonaService {

    PersonaDto createPersona(PersonaCreateDto personaCreateDto);

    List<PersonaDto> findAllPersonas();

    PersonaDto findPersonaById(UUID id);

    PersonaDto updatePersona(UUID id, PersonaCreateDto personaCreateDto);

    void deletePersona(UUID id);
}