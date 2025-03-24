package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.dto.AccountDTO;
import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.model.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/default")
@Tag(name = "Endpoints Cuentas", description = "Endpoints para la gestión de cuentas")
public interface IAccountController {
    @Operation(summary = "Cuenta de cliente", description = "Método para obtener una cuenta específica de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Si existe la cuenta para el cliente"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "404", description = "Si no existe la cuenta"),
            @ApiResponse(responseCode = "409", description = "Si la cuenta indicanda no pertenece al cliente que viene en el request")
    })
    @GetMapping (value = "/{accountId}/{ownerId}")
    ResponseEntity <AccountDTO> entregarCuenta(@PathVariable("accountId") Long accountId, @PathVariable("ownerId") Long ownerId);

    @Operation(summary = "Lista de cuentas", description = "Método para obtener todas las cuentas de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Si existen cuentas para el cliente"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "404", description = "Si el cliente no tiene cuentas")

    })
    @GetMapping ()
    ResponseEntity <Collection <AccountDTO>> obtenerCuentas(@RequestParam("customerId") Long customerId);

    @Operation(summary = "Crea una cuenta de un cliente", description = "Método para crear un cuenta nueva para un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuando crea la cuenta correctamente"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "500", description = "Si ocurre un error al crear la cuenta")
    })
    @PostMapping (consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity <AccountDTO> crearCuenta(@RequestBody Account account);

    @Operation(summary = "Elimina una cuenta por su id", description = "Método para eliminar una cuenta por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuando elimina la cuenta correctamente"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "404", description = "Si existe la cuenta que se quiere eliminar")
    })
    @DeleteMapping (value = "/{accountId}")
    ResponseEntity eliminarCuenta(@PathVariable("accountId") Long accountId);

    @Operation(summary = "Modifica una cuenta por su id", description = "Método para modificar una cuenta por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuando modifica la cuenta correctamente"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "404", description = "Si no existe la cuenta indicada en el request"),
            @ApiResponse(responseCode = "500", description = "Si ocurre un error al modificar la cuenta")
    })
    @PutMapping (value = "/{accountId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity <AccountDTO> modificarCuenta(@PathVariable("accountId") Long accountId, @RequestBody Account account);

    @Operation(summary = "Elimina una cuenta para un cliente específico", description = "Método para eliminar una cuenta para un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuando elimina la cuenta correctamente"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "404", description = "Si no existe la cuenta indicada en el request"),
            @ApiResponse(responseCode = "409", description = "Si la cuenta pertenece a un cliente distinto al indicado en el request")
    })
    @DeleteMapping (value = "/{accountId}/{customerId}")
    ResponseEntity eliminarCuentaCliente(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId);

    @Operation(summary = "Modifica una cuenta para un cliente específico", description = "Método para modificar una cuenta para un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuando modifica la cuenta correctamente"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "404", description = "Si no existe la cuenta indicada en el request"),
            @ApiResponse(responseCode = "409", description = "Si la cuenta pertenece a un cliente distinto al indicado en el request"),
            @ApiResponse(responseCode = "500", description = "Si ocurre un error al modificar la cuenta")
    })
    @PutMapping (value = "/{accountId}/{customerId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity <AccountDTO> modificarCuentaCliente(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId, @RequestBody Account account);

    @Operation(summary = "Realizar operacion en cuenta", description = "Realiza una operacion de ingresar o retirar capital de la cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuando la operación se ha realizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "404", description = "Si no existe la cuenta indicada en el request"),
            @ApiResponse(responseCode = "409", description = "Si la cuenta pertenece a un cliente distinto al indicado en el request"),
            @ApiResponse(responseCode = "500", description = "Si ocurre un error al realizar la operación")
    })
    @PutMapping (value = "/{accountId}/{customerId}/operar")
    ResponseEntity <AccountDTO> operarCuenta(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId, @RequestParam("cantidad") Double cantidad, @RequestParam("accion") AccountAction accion);

    @Operation(summary = "Elimiar cuentas del cliente", description = "Método para eliminar todas las cuentas de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuando elimina correctamente las cuentas"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente")
    })
    @DeleteMapping (value = "/{customerId}/all")
    ResponseEntity eliminarCuentasCliente(@PathVariable("customerId") Long customerId);

    @Operation(summary = "Validar viabilidad prestamo", description = "Método para validar si un cliente puede solicitar un préstamo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Si la cantidad total de capital que tiene el cliente supera al 80% de la cantidad solicitada"),
            @ApiResponse(responseCode = "400", description = "Si no se recibe la información del request correctamente"),
            @ApiResponse(responseCode = "409", description = "Si la cantidad total de capital que tiene el cliente es inferior al 80% de la cantidad solicitada")
    })
    @GetMapping (value = "/{customerId}/validar")
    ResponseEntity validarPrestamo(@PathVariable("customerId") Long customerId, @RequestParam("cantidadSolicitada") Double cantidadSolicitada);
}
