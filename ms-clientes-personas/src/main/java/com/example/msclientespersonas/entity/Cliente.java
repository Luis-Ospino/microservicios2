package com.example.msclientespersonas.entity;

import com.example.msclientespersonas.entity.enums.EstadoCliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "clientes", indexes = {
    @Index(name = "idx_clientes_persona_id", columnList = "persona_id"),
    @Index(name = "idx_clientes_estado", columnList = "estado")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Cliente extends AuditableEntity {

    @NotNull(message = "El ID de la persona es requerido")
    @Column(name = "persona_id", nullable = false, columnDefinition = "UUID")
    private UUID personaId;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
    @Column(nullable = false, length = 255)
    private String contrasena;

    @NotNull(message = "El estado es requerido")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoCliente estado = EstadoCliente.ACTIVO;
}