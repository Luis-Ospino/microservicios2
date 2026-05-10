package com.example.msclientespersonas.controller;

import com.example.msclientespersonas.dto.PersonaCreateDto;
import com.example.msclientespersonas.dto.PersonaDto;
import com.example.msclientespersonas.service.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @PostMapping
    public ResponseEntity<PersonaDto> createPersona(@Valid @RequestBody PersonaCreateDto personaCreateDto) {
        PersonaDto personaDto = personaService.createPersona(personaCreateDto);
        return ResponseEntity.created(URI.create("/api/personas/" + personaDto.getId()))
                .body(personaDto);
    }

    @GetMapping
    public ResponseEntity<List<PersonaDto>> findAllPersonas() {
        return ResponseEntity.ok(personaService.findAllPersonas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDto> findPersonaById(@PathVariable UUID id) {
        return ResponseEntity.ok(personaService.findPersonaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaDto> updatePersona(@PathVariable UUID id, @Valid @RequestBody PersonaCreateDto personaCreateDto) {
        return ResponseEntity.ok(personaService.updatePersona(id, personaCreateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable UUID id) {
        personaService.deletePersona(id);
        return ResponseEntity.noContent().build();
    }
}