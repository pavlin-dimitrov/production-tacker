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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/createOrder")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new OrderNum());
        return "addOrder"; // Името на HTML файла за създаване на поръчка
    }

    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute OrderNum orderNum,
                            RedirectAttributes redirectAttributes) {
        OrderNum savedOrder = orderService.createOrder(orderNum);
        redirectAttributes.addAttribute("orderId", savedOrder.getId());
        return "redirect:/addItems"; // Предаване на ID на поръчката към страницата за добавяне на артикули
    }

    @GetMapping("/addItems")
    public String showAddItemsForm(@RequestParam("orderId") Long orderId, Model model) {
        // Извличане на вече добавените артикули за тази поръчка
        List<OrderItem> existingItems = orderItemService.getOrderItemsByOrderId(orderId);
        Set<String> addedItemTypes = existingItems.stream().map(item -> item.getType().toString()).collect(Collectors.toSet());

        model.addAttribute("addedItems", addedItemTypes); // Актуализиран списък с вече добавени типове артикули
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderItem", new OrderItem());

        if (addedItemTypes.size() == ItemType.values().length) {
            model.addAttribute("message", "All items have been added. Please click 'FINISH ORDER'.");
        }

        return "addItems";
    }
//    @GetMapping("/addItems")
//    public String showAddItemsForm(@RequestParam("orderId") Long orderId,
//                                   Model model,
//                                   HttpSession session) {
//        Set<String> addedItems = (Set<String>) session.getAttribute("addedItems");
//        if (addedItems == null) {
//            addedItems = new HashSet<>();
//            session.setAttribute("addedItems", addedItems);
//        }
//
//        if (addedItems.size() == ItemType.values().length) {
//            model.addAttribute("message", "All items have been added. Please click 'FINISH ORDER'.");
//        }
//
//        model.addAttribute("addedItems", addedItems);
//        model.addAttribute("orderId", orderId);
//        model.addAttribute("orderItem", new OrderItem());
//        return "addItems";
//    }

    @PostMapping("/addItems")
    public String addItemsToOrder(@RequestParam("orderId") Long orderId,
                                  @ModelAttribute OrderItem orderItem,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {
        // Добавяне на проверка за null
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
                // Обработка на грешката, например показване на съобщение за грешка
            }

            session.setAttribute("addedItems", addedItems);
        }

        redirectAttributes.addAttribute("orderId", orderId);
        return "redirect:/addItems?orderId=" + orderId;
    }

    @GetMapping("/finishOrder")
    public String finishOrder(HttpSession session) {
        session.removeAttribute("addedItems"); // Изчистване на атрибута addedItems от сесията
        return "redirect:/"; // Редиректване към началната страница или където желаете
    }
}