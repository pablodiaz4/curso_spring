package com.myshoppingcart.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="compras")
@Builder
public class Compra {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer cid;

    @Column (name="cantidad")
    private int cantidad;

    @Column (name="fecha")
    private LocalDate fecha;

    @OneToOne
    @JoinColumn (name= "usuario")
    private Usuario usuario;

    @OneToOne
    @JoinColumn (name= "producto")
    private Producto producto;
}
