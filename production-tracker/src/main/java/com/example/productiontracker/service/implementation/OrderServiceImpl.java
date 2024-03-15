package com.example.productiontracker.service.implementation;

import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.entity.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.productiontracker.repository.OrderItemRepository;
import com.example.productiontracker.repository.OrderRepository;
import com.example.productiontracker.service.contract.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderNum> findAllOrderNum(String search) {
        if (search != null && !search.isEmpty()) {
            return orderRepository.findByOrderNumberContainingIgnoreCase(search);
        } else {
            return orderRepository.findAll(); // Връща всички поръчки, ако няма търсене
        }
    }

    @Override
    public OrderNum createOrder(OrderNum orderNum) {
        return orderRepository.save(orderNum);
    }

    @Override
    public Optional<OrderNum> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<OrderNum> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderNum updateOrder(Long id, OrderNum orderNumDetails) {
        OrderNum orderNum = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        orderNum.setOrderNumber(orderNumDetails.getOrderNumber());
        orderNum.setDetails(orderNumDetails.getDetails());
        orderNum.setComment(orderNumDetails.getComment());
        // Set other fields you wish to update
        return orderRepository.save(orderNum);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderNum addItemToOrder(Long orderId, Long itemId) {
        OrderNum orderNum = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id " + itemId));
        orderNum.getItems().add(item);
        return orderRepository.save(orderNum);
    }

    @Override
    public OrderNum removeItemFromOrder(Long orderId, Long itemId) {
        OrderNum orderNum = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));
        orderNum.getItems().removeIf(item -> item.getId().equals(itemId));
        return orderRepository.save(orderNum);
    }
}