package com.example.productiontracker.dto;

import com.example.productiontracker.entity.OrderItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderItemsFormDto {

    private List<OrderItem> items;
}