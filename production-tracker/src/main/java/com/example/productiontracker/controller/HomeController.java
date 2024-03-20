package com.example.productiontracker.controller;

import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.service.contract.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

  @Autowired private OrderService orderService;

  @GetMapping("/")
  public String home(
      Model model,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size) {
    Page<OrderNum> orderNums = orderService.findAllOrderNum(search, page, size);
    model.addAttribute("orderNums", orderNums);

    return "home";
  }
}
