package com.example.productiontracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ItemDetailDto {
  private int quantity;
  private int frames;
  private int sashes;
}
