package com.example.productiontracker.service.contract;

import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.OrderNum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {

    List<OrderNum> findAllOrderNum(String search);
    OrderNum createOrder(OrderNum orderNum);
    Optional<OrderNum> getOrderById(Long id);
    List<OrderNum> getAllOrders();
    OrderNum updateOrder(Long id, OrderNum orderNumDetails);
    void deleteOrder(Long id);
    OrderNum addItemToOrder(Long orderId, Long itemId);
    OrderNum removeItemFromOrder(Long orderId, Long itemId);
}
