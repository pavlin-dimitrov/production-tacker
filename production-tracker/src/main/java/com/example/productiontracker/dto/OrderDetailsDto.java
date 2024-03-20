package com.example.productiontracker.dto;

import com.example.productiontracker.entity.ItemType;
import lombok.Data;

import java.util.Map;

@Data
public class OrderDetailsDto {
  private String orderNumber;
  private String details;
  private Map<ItemType, ItemDetailDto> itemCounts;
}
