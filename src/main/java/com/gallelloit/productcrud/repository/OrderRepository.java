package com.gallelloit.productcrud.repository;

import com.gallelloit.productcrud.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
