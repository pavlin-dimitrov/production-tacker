package com.example.productiontracker.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class FilterCriteria {
  private String orderNumber;
  private String details;
  private String lastModifiedBy;
  private LocalDateTime startDate;
  LocalDateTime endDate;
}
