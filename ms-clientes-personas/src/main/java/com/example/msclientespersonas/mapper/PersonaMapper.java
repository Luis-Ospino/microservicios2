package com.example.msclientespersonas.mapper;

import com.example.msclientespersonas.dto.PersonaCreateDto;
import com.example.msclientespersonas.dto.PersonaDto;
import com.example.msclientespersonas.entity.Persona;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    PersonaDto toDto(Persona persona);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Persona toEntity(PersonaCreateDto personaCreateDto);
}