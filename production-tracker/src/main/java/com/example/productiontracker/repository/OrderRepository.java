package com.example.productiontracker.repository;

import com.example.productiontracker.entity.OrderNum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderNum, Long> {
    Optional<OrderNum> findByOrderNumber(String orderNumber);

    List<OrderNum> findByOrderNumberContainingIgnoreCase(String search);

    @Query("SELECT o FROM OrderNum o WHERE " +
            "(:orderNumber IS NULL OR o.orderNumber LIKE %:orderNumber%) AND " +
            "(:details IS NULL OR o.details LIKE %:details%) AND " +
            "(:comment IS NULL OR o.comment LIKE %:comment%)")
    List<OrderNum> findByOrderNumberOrDetailsOrComment(@Param("orderNumber") String orderNumber,
                                                       @Param("details") String details,
                                                       @Param("comment") String comment);

    @Query("SELECT o FROM OrderNum o WHERE " +
            "(:search IS NULL OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(:search IS NULL OR LOWER(o.details) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(:search IS NULL OR LOWER(o.comment) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<OrderNum> searchByOrderNumberOrDetailsOrComment(@Param("search") String search, Pageable pageable);
}