package com.example.productiontracker.dto;

import com.example.productiontracker.entity.ItemType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderItemDto {

    private ItemType type;
    private Integer quantity;
    private Integer frames;
    private Integer sashes;
}

