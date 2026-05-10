package com.example.msclientespersonas.repository;

import com.example.msclientespersonas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByPersonaId(UUID personaId);

    boolean existsByPersonaId(UUID personaId);
}