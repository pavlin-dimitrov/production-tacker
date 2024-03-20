package com.example.productiontracker.service.contract;

import com.example.productiontracker.entity.OrderNum;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface OrderService {

    Page<OrderNum> findAllOrderNum(String search, int page, int size);
    OrderNum createOrder(OrderNum orderNum);
    Optional<OrderNum> getOrderById(Long id);
    void updateOrder(Long id, OrderNum orderNumDetails);
    void deleteOrder(Long id);
}