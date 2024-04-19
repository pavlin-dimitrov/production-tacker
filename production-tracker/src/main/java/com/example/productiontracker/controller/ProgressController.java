package com.example.productiontracker.controller;

import com.example.productiontracker.dto.FilterCriteria;
import com.example.productiontracker.dto.OrderProgressInfo;
import com.example.productiontracker.dto.ProductionProgressDto;
import com.example.productiontracker.enums.OperationType;
import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.ProductionProgress;
import com.example.productiontracker.service.contract.OrderItemService;
import com.example.productiontracker.service.contract.ProductionProgressService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProgressController {

  @Autowired private ProductionProgressService productionProgressService;
  @Autowired private OrderItemService orderItemService;

  @GetMapping("/edit-progress/{itemId}")
  public String showProgressForm(@PathVariable Long itemId, Model model) {
    OrderItem orderItem = orderItemService.getOrderItemById(itemId)
            .orElseThrow(() -> new RuntimeException("OrderItem not found with id " + itemId));
    List<String> operations = orderItemService.getAllOperations();
    model.addAttribute("orderItem", orderItem);
    model.addAttribute("operations", operations);
    model.addAttribute("progress", new ProductionProgress());
    return "progress-form";
  }

  @PostMapping("/update-saveProgress/{itemId}")
  public String saveItemProgress(
      @PathVariable Long itemId,
      @ModelAttribute ProductionProgress progress,
      RedirectAttributes redirectAttributes) {

    OrderItem orderItem =
        orderItemService
            .getOrderItemById(itemId)
            .orElseThrow(() -> new RuntimeException("OrderItem not found with id " + itemId));

    ProductionProgressDto progressDto = new ProductionProgressDto();
    progressDto.setOrderNumber(orderItem.getOrderNum().getOrderNumber());
    progressDto.setItemType(orderItem.getType());
    progressDto.setCompletedFrames(progress.getCompletedFrames());
    progressDto.setCompletedSashes(progress.getCompletedSashes());
    progressDto.setOperation(progress.getOperation());

    productionProgressService.updateProductionProgress(progressDto);

    redirectAttributes.addFlashAttribute("successMessage", "progress.saved");
    return "redirect:/edit-progress/" + itemId;
  }

  @GetMapping("/getProgressInfo")
  @ResponseBody
  public ResponseEntity<?> getProgressInfo(
      @RequestParam("operation") OperationType operation, @RequestParam("itemId") Long itemId) {
    return ResponseEntity.ok(productionProgressService.getProgressInfo(operation, itemId));
  }

  @GetMapping("/filter")
  public String showFilterForm(Model model) {
    model.addAttribute("filterCriteria", new FilterCriteria());
    return "report-filter";
  }

  @GetMapping("/report")
  public String showReport(
      @ModelAttribute("filterCriteria") FilterCriteria filterCriteria,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size,
      Model model) {
    Page<OrderProgressInfo> reportData =
        productionProgressService.getFilteredProgress(
            filterCriteria.getOrderNumber(),
            filterCriteria.getDetails(),
            filterCriteria.getLastModifiedBy(),
            filterCriteria.getStartDate(),
            filterCriteria.getEndDate(),
            page,
            size);
    model.addAttribute("reportData", reportData);
    return "progress-report";
  }

  @GetMapping("/export/excel")
  public void exportToExcel(
      HttpServletResponse response, @ModelAttribute("filterCriteria") FilterCriteria filterCriteria)
      throws IOException {
    ByteArrayInputStream stream =
        productionProgressService.exportReportToExcel(
            filterCriteria.getOrderNumber(),
            filterCriteria.getDetails(),
            filterCriteria.getLastModifiedBy(),
            filterCriteria.getStartDate(),
            filterCriteria.getEndDate());

    response.setContentType("application/octet-stream");
    response.setHeader(
        "Content-Disposition",
        "attachment; filename=" + productionProgressService.createReportName(filterCriteria));

    FileCopyUtils.copy(stream, response.getOutputStream());
    response.flushBuffer();
  }
}
