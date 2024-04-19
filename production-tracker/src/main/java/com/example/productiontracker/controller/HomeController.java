package com.example.productiontracker.controller;

import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.service.contract.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.List;

@Controller
public class HomeController {

  @Autowired private OrderService orderService;

  @GetMapping("/")
  public String home(
      Model model,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size,
      Authentication authentication) {
    Page<OrderNum> orderNums = orderService.findAllOrderNum(search, page, size);
    model.addAttribute("orderNums", orderNums);

    boolean isAdmin =
        authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    model.addAttribute("isAdmin", isAdmin);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      System.out.println("Username: " + auth.getName());
      model.addAttribute("username", auth.getName());
    } else {
      System.out.println("Authentication object is null");
    }

    Calendar calendar = Calendar.getInstance();
    int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
    model.addAttribute("weekNumber", weekNumber);

    return "home";
  }
}
