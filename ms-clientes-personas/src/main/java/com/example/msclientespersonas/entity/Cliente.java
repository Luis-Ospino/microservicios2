package com.example.msclientespersonas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String identificacion;

    @Column(nullable = false, length = 100)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String contrasena;

    @Column(nullable = false)
    private Boolean estado = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}