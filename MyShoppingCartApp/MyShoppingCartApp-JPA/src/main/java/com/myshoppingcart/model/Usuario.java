package com.myshoppingcart.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="usuarios")
@Builder
public class Usuario {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer uid;

    @Column(name="nombre")
    private String nombre;

    @Column (name="apellido")
    private String apellido;

    @Column (name="email")
    private String email;

    @Column (name="interes")
    private int interes;

    @Column (name="saldo")
    private double saldo;

    @Column (name="password")
    private String password;

    @Column (name="nacimiento")
    private LocalDate nacimiento;

    @Column (name="activo")
    private boolean activo;
}
