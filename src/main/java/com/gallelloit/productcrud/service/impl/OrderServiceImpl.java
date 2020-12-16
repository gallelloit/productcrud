package com.gallelloit.productcrud.service.impl;

import com.gallelloit.productcrud.model.Order;
import com.gallelloit.productcrud.model.Product;
import com.gallelloit.productcrud.repository.OrderRepository;
import com.gallelloit.productcrud.repository.ProductRepository;
import com.gallelloit.productcrud.service.OrderService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

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

        order.setProducts(order.getProducts().stream()
                .map(product -> {

                    if (product.getProductId() == 0){
                        return productRepository.save(product);
                    }else {
                        Optional<Product> op = productRepository.findById(product.getProductId());

                        if (op.isPresent()){
                            return product;
                        } else {
                            product.setProductId(0);
                            return productRepository.save(product);
                        }

                    }
                }).collect(Collectors.toList()));

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
