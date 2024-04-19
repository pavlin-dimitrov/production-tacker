package com.example.productiontracker.dto;

import com.example.productiontracker.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {
  private String username;
  private String password;
  private Role role;
}
