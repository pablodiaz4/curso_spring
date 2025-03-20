package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.enums.AccountAction;
import com.microcompany.accountsservice.exception.AccountNotBalanceException;
import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController implements IAccountController{
    @Autowired
    private IAccountService accountService;

    @Override
    public ResponseEntity obtenerCuentas(Long customerId) {
        // Comprobamos que el id del cliente viene informado
        if (customerId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<Account> cuentas = accountService.getAccountByOwnerId(customerId);

        // Si existen cuentas, las devolvemos. En caso contrario devolvemos un NOT_FOUND
        return !CollectionUtils.isEmpty(cuentas) ? ResponseEntity.ok().body(cuentas) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se han encontrado cuentas para el cliente con id " + customerId);
    }

    @Override
    public ResponseEntity crearCuenta(Account account) {
        // Comprobamos que la cuenta venga informada
        if (account == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La información de la cuenta a crear no puede venir vacía");
        }

        // Comprobamos que la cuenta tenga un propietario
        if (account.getOwnerId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La información del propietario de la cuenta no puede venir vacio");
        }

        Account newAccount = accountService.create(account);

        // Si crea correctamente la cuenta, la devolvemos. En caso contrario devolvemos un CONFLICT
        return newAccount != null && newAccount.getId()>0 ? ResponseEntity.ok().body(newAccount) : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Override
    public ResponseEntity eliminarCuenta(Long accountId) {
        // Comprobamos que el id de cuenta venga informado
        if (accountId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador de la cuenta debe de venir informado");
        }

        // Comprobamos que la cuenta exista en el sistema
        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta con id " +  accountId + ", no existe en el sistema");
        }

        accountService.delete(accountId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity modificarCuenta(Long accountId, Account account) {
        // Comprobamos que la cuenta y el id de cuenta venga informada
        if (account == null || accountId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador de la cuenta y la información a modificar de la cuenta no pueden venir vacio");
        }

        // Comprobamos que la cuenta tenga un propietario
        if (account.getOwnerId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La información del propietario de la cuenta debe de venir informada");
        }

        // Comprobamos que la cuenta exista en el sistema
        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta con id " + accountId + " no existe en el sistema");
        }

        cuenta = accountService.updateAccount(accountId, account);

        // Si modifica correctamente la cuenta, la devolvemos. En caso contrario devolvemos un CONFLICT
        return cuenta != null ? ResponseEntity.ok().body(cuenta) : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Override
    public ResponseEntity eliminarCuentaCliente(Long accountId, Long customerId) {
        // Comprobamos que la id del cliente y el id de cuenta vengan informados
        if (customerId == null || accountId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador de la cuenta y del cliente deben de venir informados");
        }

        // Comprobamos que la cuenta exista en el sistema
        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta a eliminar no se encuentra en el sistema");
        }

        // Comprobamos que la cuenta pertenezca al cliente que nos llega desde el frontal
        if (!customerId.equals(cuenta.getOwnerId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La cuenta que desea eliminar no pertenece al cliente indicado");
        }

        accountService.delete(accountId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity modificarCuentaCliente(Long accountId, Long customerId, Account account) {
        // Comprobamos que la id del cliente y el id de cuenta vengan informados
        if (customerId == null || accountId == null || account == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador de la cuenta, del cliente y la información a modificar de la cuenta deben de venir informado");
        }

        // Comprobamos que la cuenta tenga un propietario
        if (account.getOwnerId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La información del propietario de la cuenta debe de venir informado");
        }

        // Comprobamos que la cuenta exista en el sistema
        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta con id " + accountId + " no existe en el sistema");
        }

        // Comprobamos que la cuenta pertenezca al cliente que nos llega desde el frontal
        if (!customerId.equals(cuenta.getOwnerId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La cuenta a modificar no pertenece al cliente indicado");
        }

        cuenta = accountService.updateAccount(accountId, account);

        // Si modifica correctamente la cuenta, la devolvemos. En caso contrario devolvemos un CONFLICT
        return (cuenta != null) ? ResponseEntity.ok().body(cuenta) : ResponseEntity.status(HttpStatus.CONFLICT).body("No se ha realizado correctamente la modificadión de la cuenta con id " + accountId);
    }

    @Override
    public ResponseEntity operarCuenta(Long accountId, Long customerId, Double cantidad, AccountAction accion) {
        // Comprobamos que el id de cliente, el id de cuenta y la accion vengan informada
        if (customerId == null || accountId == null || accion == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador de la cuenta, del cliente y la acción a realizar deben de venir informados");
        }

        // Comprobamos que exista l,a cuenta
        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta con id " + accountId + " no existe en el sistema");
        }

        // Comprobamos que la cuenta pertenezca al cliente que nos llega desde el frontal
        if (!customerId.equals(cuenta.getOwnerId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La cuenta no pertenece al cliente indicado");
        }

        // Llamamos al servicio para realizar una operación en la cuenta
        cuenta = accountService.operate(cuenta, cantidad, accion);

        // Si la operacion se ha realizado correctamente se devuelve un OK
        return cuenta != null ? ResponseEntity.ok().body(cuenta) : ResponseEntity.status(HttpStatus.CONFLICT).body("La modificación para la cuenta con id " + accountId + " no se ha realizado correctamente");
    }

    @Override
    public ResponseEntity eliminarCuentasCliente(Long customerId) {
        // Comprobamos que id del cliente venga informado
        if (customerId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador del cliente debe de venir informado");
        }

        accountService.deleteAccountsUsingOwnerId(customerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity entregarCuenta(Long accountId, Long ownerId) {
        // Comprobamos que el id de cuenta y el id del cliente vengan informados
        if (ownerId == null || accountId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador del cliente y de la cuenta deben de venir informado");
        }

        // Comprobamos que exista la cuenta
        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta con id " + accountId + " no existe en el sistema");
        }

        // Comprobamos que la cuenta pertenezca al cliente que nos llega desde el frontal
        if (!ownerId.equals(cuenta.getOwnerId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La cuenta con id " + accountId + " no pertenece al cliente indicado");
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity validarPrestamo(Long customerId, Double cantidadSolicitada) {
        // Comprobamos que el id del cliente venga informado
        if (customerId == null || cantidadSolicitada == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador del cliente y la cantidadSolicitada deben de venir informados");
        }

        // Obtenemos el maximo puede solicitar un cliente. 80% del balance total de todas sus cuentas
        Double maxPermitidoCliente = accountService.getAccountByOwnerId(customerId).stream().map(Account::getBalance).reduce(0D, Double::sum) * 0.8D;

        // Si el maximo permitido es superior al solicitado devolvemos un OK
        if (maxPermitidoCliente >= cantidadSolicitada){
            return ResponseEntity.ok().build();
        }

        // Si el maximo permitido es inferior al solicitado devolvemos un CONFLICT
        return ResponseEntity.status(HttpStatus.CONFLICT).body("El prestamo no puede ser concedido porque la cantidad solicitada supera al 80% de la cantidad total disponible del cliente");
    }
}
