package com.example.productiontracker.dto;

import com.example.productiontracker.entity.ItemType;
import com.example.productiontracker.entity.OperationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ProductionProgressDto {
  private String orderNumber;
  private ItemType itemType;
  private OperationType operation;
  private int completedFrames;
  private int completedSashes;
}
