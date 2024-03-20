package com.example.productiontracker.service.contract;

import com.example.productiontracker.dto.FilterCriteria;
import com.example.productiontracker.dto.OrderProgressInfo;
import com.example.productiontracker.dto.ProductionProgressDto;
import com.example.productiontracker.entity.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public interface ProductionProgressService {
  void updateProductionProgress(ProductionProgressDto progressDto);

  Page<OrderProgressInfo> getFilteredProgress(
      String orderNumber,
      String details,
      String lastModifiedBy,
      LocalDateTime startDate,
      LocalDateTime endDate,
      int page,
      int size);

  ByteArrayInputStream exportReportToExcel(
      String orderNumber,
      String details,
      String lastModifiedBy,
      LocalDateTime startDate,
      LocalDateTime endDate);

  String createReportName(FilterCriteria filterCriteria);

  Map<String, Integer> getProgressInfo(OperationType operation, Long itemId);
}
