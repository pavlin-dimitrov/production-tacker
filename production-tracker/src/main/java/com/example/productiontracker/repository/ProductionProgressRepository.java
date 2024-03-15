package com.example.productiontracker.repository;

import com.example.productiontracker.entity.OperationType;
import com.example.productiontracker.entity.OrderItem;
import com.example.productiontracker.entity.ProductionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductionProgressRepository extends JpaRepository<ProductionProgress, Long> {
    List<ProductionProgress> findByOrderItemAndOperation(OrderItem orderItem, OperationType operation);

    @Query("SELECT p FROM ProductionProgress p WHERE " +
            "p.orderItem = :orderItem AND " +
            "p.lastModifiedAt >= :startDate AND " +
            "p.lastModifiedAt <= :endDate")
    List<ProductionProgress> findByOrderItemAndModifiedBetween(@Param("orderItem") OrderItem orderItem,
                                                               @Param("startDate") LocalDateTime startDate,
                                                               @Param("endDate") LocalDateTime endDate);

}
