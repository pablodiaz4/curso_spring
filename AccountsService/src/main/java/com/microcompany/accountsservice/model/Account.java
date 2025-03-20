package com.microcompany.accountsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String type;

    @DateTimeFormat
    @NotNull
    Date openingDate;

    @Column
    private Double balance;

    @Column
    @NotNull
    private Long ownerId;

//    @JsonIgnore
//    @NotNull
//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name="ownerId", insertable = false, updatable = false, nullable = false)
////    @Transient
//    Customer ownerId;
}
