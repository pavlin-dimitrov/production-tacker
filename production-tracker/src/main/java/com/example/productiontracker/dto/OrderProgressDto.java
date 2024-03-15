package com.example.productiontracker.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderProgressDto {
    private String orderNumber;
    private String details;
    private List<ItemProgressDto> items;

    public OrderProgressDto(String orderNumber, String details, List<ItemProgressDto> items) {
        this.orderNumber = orderNumber;
        this.details = details;
        this.items = items;
    }
}
