package com.example.productiontracker.controller;

import com.example.productiontracker.dto.OrderItemDto;
import com.example.productiontracker.entity.ItemType;
import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.exception.OrderNotFoundException;
import com.example.productiontracker.service.contract.OrderItemService;
import com.example.productiontracker.service.contract.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class OrderController {

  @Autowired private OrderService orderService;
  @Autowired private OrderItemService orderItemService;

  @GetMapping("/createOrder")
  public String showCreateOrderForm(Model model) {
    model.addAttribute("order", new OrderNum());
    return "addOrder";
  }

  @PostMapping("/saveOrder")
  public String saveOrder(
      @ModelAttribute OrderNum orderNum, RedirectAttributes redirectAttributes) {
    OrderNum savedOrder = orderService.createOrder(orderNum);
    redirectAttributes.addAttribute("orderId", savedOrder.getId());
    return "redirect:/addItems";
  }

  @GetMapping("/addItems")
  public String showAddItemsForm(@RequestParam("orderId") Long orderId, Model model) {
    List<OrderItem> existingItems = orderItemService.getOrderItemsByOrderId(orderId);
    Set<String> addedItemTypes =
        existingItems.stream().map(item -> item.getType().toString()).collect(Collectors.toSet());

    model.addAttribute("addedItems", addedItemTypes);
    model.addAttribute("orderId", orderId);
    model.addAttribute("orderItem", new OrderItem());

    if (addedItemTypes.size() == ItemType.values().length) {
      model.addAttribute("message", "All items have been added. Please click 'FINISH ORDER'.");
    }

    return "addItems";
  }

  @PostMapping("/addItems")
  public String addItemsToOrder(
      @RequestParam("orderId") Long orderId,
      @ModelAttribute OrderItem orderItem,
      RedirectAttributes redirectAttributes,
      HttpSession session) {

    if (orderItem.getType() != null && orderItem.getQuantity() != null) {
      Set<String> addedItems = (Set<String>) session.getAttribute("addedItems");
      if (addedItems == null) {
        addedItems = new HashSet<>();
      }

      OrderItemDto orderItemDto = new OrderItemDto();
      orderItemDto.setType(orderItem.getType());
      orderItemDto.setQuantity(orderItem.getQuantity());
      orderItemDto.setFrames(orderItem.getFrames());
      orderItemDto.setSashes(orderItem.getSashes());

      try {
        orderItemService.addOrderItemToOrder(orderId, orderItemDto);
        addedItems.add(orderItem.getType().toString());
      } catch (OrderNotFoundException e) {
        e.printStackTrace();
      }

      session.setAttribute("addedItems", addedItems);
    }

    redirectAttributes.addAttribute("orderId", orderId);
    return "redirect:/addItems?orderId=" + orderId;
  }

  @GetMapping("/finishOrder")
  public String finishOrder(HttpSession session) {
    session.removeAttribute("addedItems");
    return "redirect:/";
  }
}