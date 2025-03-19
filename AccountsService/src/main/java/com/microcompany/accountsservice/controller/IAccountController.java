package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.model.Account;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/default")
public interface IAccountController {
    @PutMapping (value = "/{accountId}/{ownerId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity entregarCuenta(@PathVariable("accountId") Long accountId, @PathVariable("ownerId") Long ownerId);

    @GetMapping ()
    ResponseEntity obtenerCuentas(@RequestParam("customerId") Long customerId);

    @PostMapping ("")
    ResponseEntity crearCuenta(@RequestBody Account account);

    @DeleteMapping ("/{accountId}")
    ResponseEntity eliminarCuenta(@PathVariable("accountId") Long accountId);

    @PutMapping ("/{accountId}")
    ResponseEntity modificarCuenta(@PathVariable("accountId") Long accountId, @RequestBody Account account);

    @DeleteMapping ("/{accountId}/{customerId}")
    ResponseEntity eliminarCuentaCliente(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId);

    @PutMapping ("/{accountId}/{customerId}")
    ResponseEntity modificarCuentaCliente(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId, @RequestBody Account account);

    @PutMapping ("/{accountId}/{customerId}/operar")
    ResponseEntity operarCuenta(@PathVariable("accountId") Long accountId, @RequestParam("cantidad") Double cantidad, @RequestParam("accion") AccountAction accion);

    @DeleteMapping ("/{customerId}")
    ResponseEntity eliminarCuentasCliente(@PathVariable("customerId") Long customerId);

    @GetMapping ("/{customerId}/validar")
    ResponseEntity validarPrestamo(@RequestParam("customerId") Long customerId);
}
