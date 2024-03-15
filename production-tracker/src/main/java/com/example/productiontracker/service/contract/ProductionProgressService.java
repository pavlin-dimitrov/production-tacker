package com.example.productiontracker.service.contract;

import com.example.productiontracker.dto.OrderProgressDto;
import com.example.productiontracker.dto.ProductionProgressDto;
import com.example.productiontracker.entity.ProductionProgress;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface ProductionProgressService {
    void updateProductionProgress(ProductionProgressDto progressDto);

}