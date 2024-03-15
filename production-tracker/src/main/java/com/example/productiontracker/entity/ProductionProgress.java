package com.example.productiontracker.entity;

import com.example.productiontracker.auditor.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ProductionProgress extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OperationType operation; // Define OperationType as an enum with the operations

    private int completedFrames; // Number of completed frames
    private int completedSashes; // Number of completed sashes

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
}