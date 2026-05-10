package com.example.msclientespersonas.entity;

import com.example.msclientespersonas.entity.enums.Genero;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personas", uniqueConstraints = {
    @UniqueConstraint(columnNames = "identificacion", name = "uk_personas_identificacion")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Persona extends AuditableEntity {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "El género es requerido")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Genero genero;

    @NotNull(message = "La edad es requerida")
    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 120, message = "La edad debe ser menor a 120 años")
    @Column(nullable = false)
    private Integer edad;

    @NotBlank(message = "La identificación no puede estar vacía")
    @Size(min = 5, max = 20, message = "La identificación debe tener entre 5 y 20 caracteres")
    @Column(nullable = false, length = 20, unique = true)
    private String identificacion;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(min = 5, max = 150, message = "La dirección debe tener entre 5 y 150 caracteres")
    @Column(nullable = false, length = 150)
    private String direccion;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9+\\-\\s()]{7,20}$", message = "El teléfono tiene un formato inválido")
    @Column(nullable = false, length = 20)
    private String telefono;
}
