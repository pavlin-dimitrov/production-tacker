package com.example.productiontracker.data;

import com.example.productiontracker.entity.Account;
import com.example.productiontracker.enums.Role;
import com.example.productiontracker.repository.AccountRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) {
    // Create default admin if not exists
    createDefaultUser("chehplast", "chehplast2000", Role.ADMIN);

    // Create default user if not exists
    createDefaultUser("user", "user1234", Role.USER);
  }

  private void createDefaultUser(String username, String password, Role role) {
    Optional<Account> existingUser = accountRepository.findAccountByUsername(username);
    if (!existingUser.isPresent()) {
      // Create a new user if not found
      Account newUser = new Account();
      newUser.setUsername(username);
      newUser.setPassword(passwordEncoder.encode(password));
      newUser.setRole(role);
      accountRepository.save(newUser);
      System.out.println(role + " user created with username: " + username);
    } else {
      System.out.println(role + " user already exists with username: " + username);
    }
  }
}