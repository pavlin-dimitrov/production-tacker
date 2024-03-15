package com.example.productiontracker.dto;

import com.example.productiontracker.entity.ItemType;
import com.example.productiontracker.entity.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
public class ItemProgressDto {
    private ItemType itemType;
    private int totalFrames;
    private int totalSashes;
    private Map<OperationType, Integer> frameOperations;
    private Map<OperationType, Integer> sashOperations;
}
