package com.example.productiontracker.service.implementation;

import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.repository.OrderRepository;
import com.example.productiontracker.service.contract.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  @Autowired private final OrderRepository orderRepository;

  @Override
  public Page<OrderNum> findAllOrderNum(String search, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return orderRepository.searchByOrderNumberOrDetailsOrComment(search, pageable);
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
  public void updateOrder(Long id, OrderNum orderNumDetails) {
    OrderNum orderNum =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    orderNum.setOrderNumber(orderNumDetails.getOrderNumber());
    orderNum.setDetails(orderNumDetails.getDetails());
    orderNum.setComment(orderNumDetails.getComment());
    orderRepository.save(orderNum);
  }

  @Override
  public void deleteOrder(Long id) {
    orderRepository.deleteById(id);
  }
}
