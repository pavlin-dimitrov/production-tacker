package com.example.productiontracker.repository;

import com.example.productiontracker.entity.ProductionProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductionProgressRepository extends JpaRepository<ProductionProgress, Long> {

  @Query(
      "SELECT pp FROM ProductionProgress pp WHERE "
          + "(:orderNumber IS NULL OR pp.orderItem.orderNum.orderNumber = :orderNumber) OR "
          + "(:details IS NULL OR pp.orderItem.orderNum.details LIKE %:details%) OR "
          + "(:lastModifiedBy IS NULL OR pp.lastModifiedBy = :lastModifiedBy) OR "
          + "(COALESCE(:startDate, NULL) IS NULL OR pp.lastModifiedAt >= :startDate) OR "
          + "(COALESCE(:endDate, NULL) IS NULL OR pp.lastModifiedAt <= :endDate)")
  Page<ProductionProgress> findFilteredProgress(
      String orderNumber,
      String details,
      String lastModifiedBy,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable);

  @Query(
      "SELECT pp FROM ProductionProgress pp WHERE "
          + "(:orderNumber IS NULL OR pp.orderItem.orderNum.orderNumber = :orderNumber) OR "
          + "(:details IS NULL OR pp.orderItem.orderNum.details LIKE %:details%) OR "
          + "(:lastModifiedBy IS NULL OR pp.lastModifiedBy = :lastModifiedBy) OR "
          + "(COALESCE(:startDate, NULL) IS NULL OR pp.lastModifiedAt >= :startDate) OR "
          + "(COALESCE(:endDate, NULL) IS NULL OR pp.lastModifiedAt <= :endDate)")
  List<ProductionProgress> findFilteredProgressForExcel(
      String orderNumber,
      String details,
      String lastModifiedBy,
      LocalDateTime startDate,
      LocalDateTime endDate);
}
