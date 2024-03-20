package com.example.productiontracker.service.contract;

import com.example.productiontracker.dto.OrderItemDto;
import com.example.productiontracker.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderItemService {
    OrderItem createOrderItem(OrderItem orderItem);
    Optional<OrderItem> getOrderItemById(Long id);
    List<OrderItem> getAllOrderItems();
    void updateOrderItem(Long id, OrderItem orderItemDetails);
    void deleteOrderItem(Long id);
    void addOrderItemToOrder(Long orderId, OrderItemDto orderItemDto);
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
    List<String> getAllOperations();
}