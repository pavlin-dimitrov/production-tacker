package com.example.productiontracker.service.implementation;

import com.example.productiontracker.dto.OrderItemDto;
import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.exception.OrderNotFoundException;
import com.example.productiontracker.repository.OrderRepository;
import com.example.productiontracker.service.contract.OrderItemService;
import com.example.productiontracker.entity.OrderItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.productiontracker.repository.OrderItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private final OrderItemRepository orderItemRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
    @Override
    public Optional<OrderItem> getOrderItemById(Long id) {
        return orderItemRepository.findById(id);
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }
    @Override
    public OrderItem updateOrderItem(Long id, OrderItem orderItemDetails) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id " + id));
        orderItem.setType(orderItemDetails.getType());
        orderItem.setQuantity(orderItemDetails.getQuantity());
        orderItem.setFrames(orderItemDetails.getFrames());
        orderItem.setSashes(orderItemDetails.getSashes());
        // Set other fields that you wish to update
        return orderItemRepository.save(orderItem);
    }
    @Override
    @Transactional
    public void addOrderItemToOrder(Long orderId, OrderItemDto orderItemDto) throws OrderNotFoundException {
        OrderNum order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        OrderItem orderItem = new OrderItem();

        orderItem.setType(orderItemDto.getType());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setFrames(orderItemDto.getFrames());
        orderItem.setSashes(orderItemDto.getSashes());


        orderItem.setOrderNum(order);
        order.getItems().add(orderItem);
        orderItemRepository.save(orderItem);
    }
    @Override
    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderNumId(orderId);
    }
}