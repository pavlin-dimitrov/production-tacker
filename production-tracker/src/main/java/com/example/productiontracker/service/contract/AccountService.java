package com.example.productiontracker.service.contract;

import com.example.productiontracker.dto.RegistrationDto;
import com.example.productiontracker.entity.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    Account registerNewAccount(RegistrationDto registrationDto);
}
