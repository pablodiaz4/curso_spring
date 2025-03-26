package com.microcompany.AccountsService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcompany.accountsservice.dto.AccountDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql(value = "classpath:testing.sql")
class AccountControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void entregarCuentaTestOK() throws Exception{
        MvcResult result = mvc.perform(get("/account").param("customerId", "1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        List<AccountDTO> cuentas = mapper.readValue(result.getResponse().getContentAsString(), List.class);
        Assertions.assertTrue(cuentas.size()> 0);
    }
}
