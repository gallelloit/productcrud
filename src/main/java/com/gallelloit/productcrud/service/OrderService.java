package com.gallelloit.productcrud.service;

import com.gallelloit.productcrud.model.Order;
import javassist.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    public long count();

    int save(Order order) throws Exception;

    Order findById(int orderId) throws NotFoundException;

    List<Order> findByDatesRange(LocalDateTime startDate, LocalDateTime endDate);
}
