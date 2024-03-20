package com.example.productiontracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.service.contract.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderItemTypeController {

  @Autowired private OrderService orderService;

  @GetMapping("/progress/{id}")
  public String orderItemTypes(@PathVariable Long id, Model model) {
    OrderNum orderNum =
        orderService
            .getOrderById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

    model.addAttribute("orderNum", orderNum);
    return "orderItemTypes";
  }
}