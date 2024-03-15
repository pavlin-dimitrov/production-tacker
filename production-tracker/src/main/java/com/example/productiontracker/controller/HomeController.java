package com.example.productiontracker.controller;

import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.service.contract.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private OrderService orderService; // Предполага се, че имате такъв сервис

    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "search", required = false) String search) {
        List<OrderNum> orderNums = orderService.findAllOrderNum(search); // Търсенето се изпълнява в сервисния слой
        model.addAttribute("orderNums", orderNums);
        return "home"; // Връща home.html шаблон
    }
}

