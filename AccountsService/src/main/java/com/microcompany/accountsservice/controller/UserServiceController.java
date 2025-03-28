package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.User;
import com.microcompany.accountsservice.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserServiceController {
    @Autowired
    IUserService userService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody @Valid User newUser) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String enc_password = passwordEncoder.encode(newUser.getPassword());

        newUser.setId(null);
        newUser.setPassword(enc_password);

        return new ResponseEntity<>(userService.create(newUser), HttpStatus.CREATED);
    }
}
