package com.example.productiontracker.controller;

import com.example.productiontracker.dto.ItemDetailDto;
import com.example.productiontracker.dto.OrderDetailsDto;
import com.example.productiontracker.dto.OrderItemDto;
import com.example.productiontracker.entity.ItemType;
import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.service.contract.OrderItemService;
import com.example.productiontracker.service.contract.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderControllerApi {

  @Autowired private final OrderService orderService;
  @Autowired private final OrderItemService orderItemService;

  @PostMapping
  public ResponseEntity<OrderNum> createOrder(@RequestBody OrderNum orderNum) {
    OrderNum newOrderNum = orderService.createOrder(orderNum);
    return new ResponseEntity<>(newOrderNum, HttpStatus.CREATED);
  }

  @PostMapping("/{orderId}/items")
  public void addItemToOrder(@PathVariable Long orderId, @RequestBody OrderItemDto orderItemDto) {
    orderItemService.addOrderItemToOrder(orderId, orderItemDto);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderDetailsDto> getOrderDetails(@PathVariable Long orderId) {
    Optional<OrderNum> orderNumOptional = orderService.getOrderById(orderId);
    if (!orderNumOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    OrderNum orderNum = orderNumOptional.get();
    List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);

    OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
    orderDetailsDto.setOrderNumber(orderNum.getOrderNumber());
    orderDetailsDto.setDetails(orderNum.getDetails());

    Map<ItemType, ItemDetailDto> itemCounts =
        orderItems.stream()
            .collect(
                Collectors.groupingBy(
                    OrderItem::getType,
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        items -> {
                          int totalQuantity = items.stream().mapToInt(OrderItem::getQuantity).sum();
                          int totalFrames = items.stream().mapToInt(OrderItem::getFrames).sum();
                          int totalSashes = items.stream().mapToInt(OrderItem::getSashes).sum();
                          return new ItemDetailDto(totalQuantity, totalFrames, totalSashes);
                        })));
    orderDetailsDto.setItemCounts(itemCounts);

    return ResponseEntity.ok(orderDetailsDto);
  }
}