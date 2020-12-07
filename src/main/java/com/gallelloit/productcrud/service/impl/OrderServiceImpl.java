package com.gallelloit.productcrud.service.impl;

import com.gallelloit.productcrud.model.Order;
import com.gallelloit.productcrud.repository.OrderRepository;
import com.gallelloit.productcrud.service.OrderService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public long count() {
        return orderRepository.count();
    }

    @Override
    public int save(Order order) throws Exception {

        long orderId = order.getOrderId();

        if (orderId != 0){
            throw new Exception(String.format("Bad Request: orderId = %d", orderId));
        }
        if(order.getProducts().isEmpty()){
            throw new Exception(String.format("Bad Request: No products selected.", orderId));
        }

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getOrderId();
    }

    @Override
    public Order findById(int orderId) throws NotFoundException {

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (!optionalOrder.isPresent()){
            throw new NotFoundException(String.format("Order not found: %d", orderId));
        }

        return optionalOrder.get();
    }

    @Override
    public List<Order> findByDatesRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreateDateBetween(startDate, endDate);
    }
}
