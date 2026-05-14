package com.example.mscuentasmovimientos.repository;

import com.example.mscuentasmovimientos.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, UUID> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    List<Cuenta> findByClienteId(UUID clienteId);

    boolean existsByNumeroCuenta(String numeroCuenta);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cuenta c WHERE c.id = :id")
    Optional<Cuenta> findByIdForUpdate(UUID id);
}
