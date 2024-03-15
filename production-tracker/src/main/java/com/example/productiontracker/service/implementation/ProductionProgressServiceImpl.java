package com.example.productiontracker.service.implementation;

import com.example.productiontracker.dto.ItemProgressDto;
import com.example.productiontracker.dto.OrderProgressDto;
import com.example.productiontracker.dto.ProductionProgressDto;
import com.example.productiontracker.entity.OperationType;
import com.example.productiontracker.repository.OrderItemRepository;
import com.example.productiontracker.service.contract.ProductionProgressService;
import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.ProductionProgress;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.productiontracker.repository.OrderRepository;
import com.example.productiontracker.repository.ProductionProgressRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional
public class ProductionProgressServiceImpl implements ProductionProgressService {

    @Autowired
    private OrderRepository orderNumRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductionProgressRepository productionProgressRepository;

    @Override
    public void updateProductionProgress(ProductionProgressDto progressDto) {
        OrderNum orderNum = orderNumRepository.findByOrderNumber(progressDto.getOrderNumber())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with number: " + progressDto.getOrderNumber()));

        OrderItem orderItem = orderNum.getItems().stream()
                .filter(item -> item.getType() == progressDto.getItemType())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found with type: " + progressDto.getItemType()));

        //verifyProgressDoesNotExceedOrderItem(orderItem, progressDto);

        ProductionProgress progress = new ProductionProgress();
        progress.setOperation(progressDto.getOperation());
        progress.setCompletedFrames(progressDto.getCompletedFrames());
        progress.setCompletedSashes(progressDto.getCompletedSashes());
        progress.setOrderItem(orderItem);

        productionProgressRepository.save(progress);
    }

}