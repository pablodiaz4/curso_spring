package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.model.Account;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/default")
public interface IAccountController {
    @PutMapping (value = "/{accountId}/{ownerId}")
    ResponseEntity entregarCuenta(@PathVariable("accountId") Long accountId, @PathVariable("ownerId") Long ownerId);

    @GetMapping ()
    ResponseEntity obtenerCuentas(@RequestParam("customerId") Long customerId);

    @PostMapping (consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity crearCuenta(@RequestBody Account account);

    @DeleteMapping (value = "/{accountId}")
    ResponseEntity eliminarCuenta(@PathVariable("accountId") Long accountId);

    @PutMapping (value = "/{accountId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity modificarCuenta(@PathVariable("accountId") Long accountId, @RequestBody Account account);

    @DeleteMapping (value = "/{accountId}/{customerId}")
    ResponseEntity eliminarCuentaCliente(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId);

    @PutMapping (value = "/{accountId}/{customerId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity modificarCuentaCliente(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId, @RequestBody Account account);

    @PutMapping (value = "/{accountId}/{customerId}/operar")
    ResponseEntity operarCuenta(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId, @RequestParam("cantidad") Double cantidad, @RequestParam("accion") AccountAction accion);

    @DeleteMapping (value = "/{customerId}/all")
    ResponseEntity eliminarCuentasCliente(@PathVariable("customerId") Long customerId);

    @GetMapping (value = "/{customerId}/validar")
    ResponseEntity validarPrestamo(@PathVariable("customerId") Long customerId, @RequestParam("cantidadSolicitada") Double cantidadSolicitada);
}
