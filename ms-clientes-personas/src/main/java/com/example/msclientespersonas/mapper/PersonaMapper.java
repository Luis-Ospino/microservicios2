package com.example.msclientespersonas.mapper;

import com.example.msclientespersonas.dto.PersonaCreateDto;
import com.example.msclientespersonas.dto.PersonaDto;
import com.example.msclientespersonas.entity.Persona;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    PersonaDto toDto(Persona persona);

    Persona toEntity(PersonaCreateDto personaCreateDto);
}