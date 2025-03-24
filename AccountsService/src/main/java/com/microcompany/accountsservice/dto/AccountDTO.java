package com.microcompany.accountsservice.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(name = "Cuenta", description = "La cuenta")
public class AccountDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Id", description = "El identificador de la cuenta", example = "345", format = "int64")
    private Long id;

    @NotBlank
    private String tipo;

    @DateTimeFormat
    @NotBlank
    Date fechaApertura;

    private Double saldo;

    @NotBlank
    private Long clienteId;
}
