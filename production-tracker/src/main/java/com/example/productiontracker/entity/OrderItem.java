package com.example.productiontracker.entity;

import com.example.productiontracker.auditor.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class OrderItem extends Auditable<String> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ItemType type;

  private Integer quantity;
  private Integer frames;
  private Integer sashes;

  @ManyToOne
  @JoinColumn(
      name =
          "order_num_id")
  private OrderNum orderNum;

  @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductionProgress> progress = new ArrayList<>();
}
