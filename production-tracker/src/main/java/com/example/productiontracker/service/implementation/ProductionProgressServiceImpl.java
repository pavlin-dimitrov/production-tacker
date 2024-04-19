package com.example.productiontracker.service.implementation;

import com.example.productiontracker.dto.FilterCriteria;
import com.example.productiontracker.dto.OrderProgressInfo;
import com.example.productiontracker.dto.ProductionProgressDto;
import com.example.productiontracker.enums.OperationType;
import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.OrderNum;
import com.example.productiontracker.entity.ProductionProgress;
import com.example.productiontracker.repository.OrderRepository;
import com.example.productiontracker.repository.ProductionProgressRepository;
import com.example.productiontracker.service.contract.OrderItemService;
import com.example.productiontracker.service.contract.ProductionProgressService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductionProgressServiceImpl implements ProductionProgressService {

  @Autowired private OrderRepository orderNumRepository;
  @Autowired private OrderItemService orderItemService;
  @Autowired private ProductionProgressRepository productionProgressRepository;

  @Override
  public void updateProductionProgress(ProductionProgressDto progressDto) {
    OrderNum orderNum =
        orderNumRepository
            .findByOrderNumber(progressDto.getOrderNumber())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Order not found with number: " + progressDto.getOrderNumber()));

    OrderItem orderItem =
        orderNum.getItems().stream()
            .filter(item -> item.getType() == progressDto.getItemType())
            .findFirst()
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Item not found with type: " + progressDto.getItemType()));

    ProductionProgress progress = new ProductionProgress();
    progress.setOperation(progressDto.getOperation());
    progress.setCompletedFrames(progressDto.getCompletedFrames());
    progress.setCompletedSashes(progressDto.getCompletedSashes());
    progress.setOrderItem(orderItem);

    productionProgressRepository.save(progress);
  }

  public Page<OrderProgressInfo> getFilteredProgress(
      String orderNumber,
      String details,
      String lastModifiedBy,
      LocalDateTime startDate,
      LocalDateTime endDate,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<ProductionProgress> progressPage =
        productionProgressRepository.findFilteredProgress(
            orderNumber, details, lastModifiedBy, startDate, endDate, pageable);
    return progressPage.map(this::convertToDto);
  }

  private OrderProgressInfo convertToDto(ProductionProgress progress) {
    OrderProgressInfo dto = new OrderProgressInfo();

    OrderItem orderItem = progress.getOrderItem();
    OrderNum orderNum = orderItem.getOrderNum();

    dto.setOrderNumber(orderNum.getOrderNumber());
    dto.setDetails(orderNum.getDetails());
    dto.setComment(orderNum.getComment());
    dto.setType(orderItem.getType());
    dto.setLastModifiedBy(progress.getLastModifiedBy());
    dto.setLastModifiedAt(progress.getLastModifiedAt());
    dto.setCompletedFrames(progress.getCompletedFrames());
    dto.setCompletedSashes(progress.getCompletedSashes());
    dto.setOperation(progress.getOperation());
    return dto;
  }

  public ByteArrayInputStream exportReportToExcel(
      String orderNumber,
      String details,
      String lastModifiedBy,
      LocalDateTime startDate,
      LocalDateTime endDate) {
    List<ProductionProgress> reportData =
        productionProgressRepository.findFilteredProgressForExcel(
            orderNumber, details, lastModifiedBy, startDate, endDate);

    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream(); ) {
      Sheet sheet = getRows(workbook);
      int rowIdx = 1;
      for (ProductionProgress progress : reportData) {
        rowIdx = getRowIdx(progress, sheet, rowIdx);
      }
      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("Failed to export data to Excel file: " + e.getMessage());
    }
  }

  private static int getRowIdx(ProductionProgress progress, Sheet sheet, int rowIdx) {
    Row row = sheet.createRow(rowIdx++);

    OrderItem orderItem = progress.getOrderItem();
    OrderNum orderNum = orderItem.getOrderNum();

    row.createCell(0).setCellValue(orderNum.getOrderNumber());
    row.createCell(1).setCellValue(orderNum.getDetails());
    row.createCell(2).setCellValue(orderNum.getComment());
    row.createCell(3).setCellValue(orderItem.getType().name());
    row.createCell(4).setCellValue(progress.getLastModifiedBy());
    row.createCell(5).setCellValue(progress.getLastModifiedAt());
    row.createCell(6).setCellValue(progress.getCompletedFrames());
    row.createCell(7).setCellValue(progress.getCompletedSashes());
    row.createCell(8).setCellValue(progress.getOperation().name());
    return rowIdx;
  }

  @NotNull
  private static Sheet getRows(Workbook workbook) {
    Sheet sheet = workbook.createSheet("Report");
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("Order Number");
    headerRow.createCell(1).setCellValue("Details");
    headerRow.createCell(2).setCellValue("Comments");
    headerRow.createCell(3).setCellValue("Item Type");
    headerRow.createCell(4).setCellValue("User");
    headerRow.createCell(5).setCellValue("Date");
    headerRow.createCell(6).setCellValue("Frames");
    headerRow.createCell(7).setCellValue("Sashes");
    headerRow.createCell(8).setCellValue("Operation");
    return sheet;
  }

  @Override
  public String createReportName(FilterCriteria filterCriteria) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedStartDate = filterCriteria.getStartDate().format(formatter);
    String formattedEndDate = filterCriteria.getEndDate().format(formatter);

    return "Report - "
        + filterCriteria.getOrderNumber()
        + " - "
        + filterCriteria.getDetails()
        + " - "
        + filterCriteria.getLastModifiedBy()
        + " - "
        + formattedStartDate
        + " - "
        + formattedEndDate
        + ".xlsx";
  }

  public Map<String, Integer> getProgressInfo(OperationType operation, Long itemId) {

    OrderItem orderItem =
        orderItemService
            .getOrderItemById(itemId)
            .orElseThrow(() -> new RuntimeException("OrderItem not found with id " + itemId));

    List<ProductionProgress> progressList =
        orderItem.getProgress().stream()
            .filter(progress -> progress.getOperation().equals(operation))
            .toList();

    int totalCompletedFrames =
        progressList.stream().mapToInt(ProductionProgress::getCompletedFrames).sum();
    int totalCompletedSashes =
        progressList.stream().mapToInt(ProductionProgress::getCompletedSashes).sum();

    Map<String, Integer> progressInfo = new HashMap<>();
    progressInfo.put("completedFrames", totalCompletedFrames);
    progressInfo.put("completedSashes", totalCompletedSashes);

    return progressInfo;
  }
}
