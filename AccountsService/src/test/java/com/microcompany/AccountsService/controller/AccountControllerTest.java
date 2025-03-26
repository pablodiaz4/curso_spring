package com.microcompany.AccountsService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcompany.accountsservice.dto.AccountDTO;
import com.microcompany.accountsservice.exception.AccountNotfoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql(value = "classpath:testing.sql")
public class AccountControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void obtenerCuentasClienteCustomerIdValidReturnOKAndAccountList() throws Exception{
        MvcResult result = mvc.perform(get("/account").param("customerId", "1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertNotNull(result.getResponse());
        Assertions.assertNotNull(result.getResponse().getStatus());
        Assertions.assertEquals(200, result.getResponse().getStatus());

        ObjectMapper mapper = new ObjectMapper();
        List<AccountDTO> cuentas = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        Assertions.assertTrue(cuentas.size()> 0);
    }

    @Test
    public void obtenerCuentasClienteCustomerIdNoHaveAccountReturnNotFound() throws Exception{
        MvcResult result = mvc.perform(get("/account").param("customerId", "99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        Assertions.assertNotNull(result.getResponse());
        Assertions.assertNotNull(result.getResponse().getStatus());
        Assertions.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    public void obtenerCuentasClienteCustomerIdIsNullReturnBadRequest() throws Exception{
        MvcResult result = mvc.perform(get("/account").param("customerId", "").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        Assertions.assertNotNull(result.getResponse());
        Assertions.assertNotNull(result.getResponse().getStatus());
        Assertions.assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void entregarCuentaTestOwnerIdIsNotNullAndAccountIdIsNotNullReturnOKAndAccount() throws Exception{
        MvcResult result = mvc.perform(get("/account/{accountId}/{ownerId}", "1", "1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertNotNull(result.getResponse());
        Assertions.assertNotNull(result.getResponse().getStatus());
        Assertions.assertEquals(200, result.getResponse().getStatus());

        ObjectMapper mapper = new ObjectMapper();
        AccountDTO cuenta = mapper.readValue(result.getResponse().getContentAsString(), AccountDTO.class);
        Assertions.assertNotNull(cuenta);
        Assertions.assertEquals(1,cuenta.getId());
        Assertions.assertEquals(1,cuenta.getClienteId());
    }

    @Test
    public void entregarCuentaTestAccountIdIsNullReturnBadRequest() throws Exception{
        MvcResult result = mvc.perform(get("/account/{accountId}/{ownerId}", "null", "1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        Assertions.assertNotNull(result.getResponse());
        Assertions.assertNotNull(result.getResponse().getStatus());
        Assertions.assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void entregarCuentaTestOwnerIdIsNullReturnBadRequest() throws Exception{
        MvcResult result = mvc.perform(get("/account/{accountId}/{ownerId}", "1", "null").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();

        Assertions.assertNotNull(result.getResponse());
        Assertions.assertNotNull(result.getResponse().getStatus());
        Assertions.assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void entregarCuentaTestAccountIdNoExistReturnNotFoundException() throws Exception{
        assertThatThrownBy(
                        () -> mvc.perform(MockMvcRequestBuilders.get("/account/{accountId}/{ownerId}", "99", "1").accept(MediaType.APPLICATION_JSON)))
                .hasCauseInstanceOf(AccountNotfoundException.class).hasMessageContaining("Account with id: 99 not found");
    }

    @Test
    public void entregarCuentaTestOwnerIdRequestNotEqualOwnerIdAccountReturnConflict() throws Exception{
        MvcResult result = mvc.perform(get("/account/{accountId}/{ownerId}", "1", "2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()).andReturn();

        Assertions.assertNotNull(result.getResponse());
        Assertions.assertNotNull(result.getResponse().getStatus());
        Assertions.assertEquals(409, result.getResponse().getStatus());
    }
}
