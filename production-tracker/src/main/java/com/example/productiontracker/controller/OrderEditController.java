package com.example.productiontracker.controller;

import com.example.productiontracker.dto.OrderItemsFormDto;
import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.service.contract.OrderItemService;
import com.example.productiontracker.service.contract.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderEditController {

  @Autowired private OrderService orderService;
  @Autowired private OrderItemService orderItemService;

  @GetMapping("/editOrder/{id}")
  public String showEditOrderForm(@PathVariable("id") Long id, Model model) {
    OrderNum orderNum =
        orderService
            .getOrderById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

    List<OrderItem> items = orderItemService.getOrderItemsByOrderId(id);
    orderNum.setItems(items);

    model.addAttribute("orderNum", orderNum);
    return "edit-order";
  }

  @PostMapping("/updateOrder/{id}")
  public String updateOrder(
      @PathVariable("id") Long id,
      @ModelAttribute("order") OrderNum orderNum,
      RedirectAttributes redirectAttributes) {
    orderService.updateOrder(id, orderNum);
    redirectAttributes.addFlashAttribute("success", "Order updated successfully");
    return "redirect:/";
  }

  @GetMapping("/editItems/{orderId}")
  public String showEditItemsForm(@PathVariable("orderId") Long orderId, Model model) {
    OrderNum orderNum =
        orderService
            .getOrderById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));

    OrderItemsFormDto form = new OrderItemsFormDto();
    form.setItems(new ArrayList<>(orderNum.getItems()));

    model.addAttribute("form", form);
    model.addAttribute("orderNum", orderNum);
    return "edit-items";
  }

  @PostMapping("/updateItems/{orderId}")
  public String updateItems(
      @ModelAttribute("form") OrderItemsFormDto form, RedirectAttributes redirectAttributes) {
    for (OrderItem item : form.getItems()) {
      OrderItem existingItem =
          orderItemService
              .getOrderItemById(item.getId())
              .orElseThrow(() -> new RuntimeException("Item not found with id " + item.getId()));
      existingItem.setQuantity(item.getQuantity());
      orderItemService.updateOrderItem(existingItem.getId(), existingItem);
    }

    redirectAttributes.addFlashAttribute("success", "Items updated successfully");
    return "redirect:/";
  }

  @GetMapping("/deleteItem/{orderId}/{itemId}")
  public String deleteOrderItem(
      @PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId) {

    orderItemService.deleteOrderItem(itemId);

    List<OrderItem> remainingItems = orderItemService.getOrderItemsByOrderId(orderId);
    if (remainingItems.isEmpty()) {
      return "redirect:/";
    } else {
      return "redirect:/editItems/" + orderId;
    }
  }

  @GetMapping("/deleteOrder/{id}")
  public String deleteOrder(@PathVariable("id") Long id) {
    orderService.deleteOrder(id);
    return "redirect:/";
  }
}