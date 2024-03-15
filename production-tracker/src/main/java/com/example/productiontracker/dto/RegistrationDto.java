package com.example.productiontracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {
    private String username;
    private String password;
    // Можете да добавите други полета, които са необходими при регистрация, като email, име и т.н.
}

