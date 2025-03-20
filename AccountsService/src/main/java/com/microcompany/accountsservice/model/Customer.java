package com.microcompany.accountsservice.model;


import javax.persistence.*;

import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String email;

//    @OneToMany (fetch = FetchType.LAZY)
//    private List<Account> cuentas;
}
