package com.example.productiontracker.dto;

import com.example.productiontracker.entity.ItemType;
import com.example.productiontracker.enums.OperationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderProgressInfo {
  private String orderNumber;
  private String details;
  private String comment;
  private ItemType type;
  private String lastModifiedBy;
  private LocalDateTime lastModifiedAt;
  private int completedFrames;
  private int completedSashes;
  private OperationType operation;
}
