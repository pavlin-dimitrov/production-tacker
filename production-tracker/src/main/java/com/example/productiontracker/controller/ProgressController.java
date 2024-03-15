package com.example.productiontracker.controller;

import com.example.productiontracker.dto.OrderProgressDto;
import com.example.productiontracker.dto.ProductionProgressDto;
import com.example.productiontracker.entity.OperationType;
import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.ProductionProgress;
import com.example.productiontracker.service.contract.OrderItemService;
import com.example.productiontracker.service.contract.ProductionProgressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProgressController {

    @Autowired
    private ProductionProgressService productionProgressService;
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/edit-progress/{itemId}")
    public String showProgressForm(@PathVariable Long itemId, Model model) {
        OrderItem orderItem = orderItemService.getOrderItemById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id " + itemId));

        List<String> operations = new ArrayList<>();
        operations.add("Select Operation..."); // Първа стойност в списъка
        operations.addAll(Arrays.stream(OperationType.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        model.addAttribute("orderItem", orderItem);
        model.addAttribute("operations", operations);
        model.addAttribute("progress", new ProductionProgress());
        return "progress-form";
    }


    @PostMapping("/update-saveProgress/{itemId}")
    public String saveItemProgress(@PathVariable Long itemId, @ModelAttribute ProductionProgress progress,
                                   Model model, RedirectAttributes redirectAttributes) {
        OrderItem orderItem = orderItemService.getOrderItemById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id " + itemId));

        ProductionProgressDto progressDto = new ProductionProgressDto();
        progressDto.setOrderNumber(orderItem.getOrderNum().getOrderNumber());
        progressDto.setItemType(orderItem.getType());
        progressDto.setCompletedFrames(progress.getCompletedFrames());
        progressDto.setCompletedSashes(progress.getCompletedSashes());
        progressDto.setOperation(progress.getOperation());

        productionProgressService.updateProductionProgress(progressDto);

        redirectAttributes.addFlashAttribute("successMessage", "progress.saved");
        return "redirect:/edit-progress/" + itemId; // Пренасочва обратно към страницата за редактиране на прогреса
    }

    @GetMapping("/getProgressInfo")
    @ResponseBody
    public ResponseEntity<?> getProgressInfo(@RequestParam("operation") OperationType operation, @RequestParam("itemId") Long itemId) {
        // Намиране на артикула по itemId
        OrderItem orderItem = orderItemService.getOrderItemById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id " + itemId));

        // Филтриране на прогреса на артикула по зададената операция
        List<ProductionProgress> progressList = orderItem.getProgress().stream()
                .filter(progress -> progress.getOperation().equals(operation))
                .collect(Collectors.toList());

        // Изчисляване на сумата от завършените рамки и крила за избраната операция
        int totalCompletedFrames = progressList.stream().mapToInt(ProductionProgress::getCompletedFrames).sum();
        int totalCompletedSashes = progressList.stream().mapToInt(ProductionProgress::getCompletedSashes).sum();

        Map<String, Integer> progressInfo = new HashMap<>();
        progressInfo.put("completedFrames", totalCompletedFrames);
        progressInfo.put("completedSashes", totalCompletedSashes);

        return ResponseEntity.ok(progressInfo);
    }

}