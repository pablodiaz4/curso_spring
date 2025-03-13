package com.myshoppingcart.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="productos")
@Builder
public class Producto {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer mid;

    @Column (name="codigo")
    private String codigo;

    @Column (name="marca")
    private String marca;

    @Column (name="tipo")
    private String tipo;

    @Column (name="nombre")
    private String nombre;

    @Column (name="precio")
    private double precio;

    @Column (name="existencias")
    private int existencias;

    public Producto(Integer id, String cod, double prec) {
        this.mid = id;
        this.codigo = cod;
        this.precio = prec;
    }
}
