package com.example.productiontracker.controller;

import com.example.productiontracker.dto.ProductionProgressDto;
import com.example.productiontracker.service.contract.ProductionProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
public class ProgressControllerApi {

    private final ProductionProgressService productionProgressService;

    @Autowired
    public ProgressControllerApi(ProductionProgressService productionProgressService) {
        this.productionProgressService = productionProgressService;
    }
    @PostMapping("/update")
    public ResponseEntity<Void> updateProgress(@RequestBody ProductionProgressDto progressDto) {
        productionProgressService.updateProductionProgress(progressDto);
        return ResponseEntity.ok().build();
    }
}