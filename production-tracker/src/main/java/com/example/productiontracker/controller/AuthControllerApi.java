package com.example.productiontracker.controller;

import com.example.productiontracker.dto.RegistrationDto;
import com.example.productiontracker.entity.Account;
import com.example.productiontracker.service.contract.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControllerApi {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody RegistrationDto registrationDto) {
        Account registeredAccount = accountService.registerNewAccount(registrationDto);
        return new ResponseEntity<>(registeredAccount, HttpStatus.CREATED);
    }
}