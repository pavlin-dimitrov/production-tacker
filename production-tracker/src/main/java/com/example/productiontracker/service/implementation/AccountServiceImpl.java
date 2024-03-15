package com.example.productiontracker.service.implementation;

import com.example.productiontracker.dto.RegistrationDto;
import com.example.productiontracker.entity.Account;
import com.example.productiontracker.repository.AccountRepository;
import com.example.productiontracker.service.contract.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Account registerNewAccount(RegistrationDto registrationDto) {
        if(accountRepository.findAccountByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Account account = new Account();
        account.setUsername(registrationDto.getUsername());
        account.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        // Можете да добавите и други полета от DTO към Account тук
        return accountRepository.save(account);
    }
}