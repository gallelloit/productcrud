package com.gallelloit.productcrud.controller;

import com.gallelloit.productcrud.model.Order;
import com.gallelloit.productcrud.service.OrderService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class OrderRestController {

    @Autowired
    OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity placeOrder(@RequestBody Order order){

        try{
            orderService.save(order);
        }catch(Exception e){
            //TODO Customize exception handling
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(order);

    }

    @GetMapping("/order/findByDatesRange")
    public ResponseEntity findOrdersByDatesRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){

        //TODO Validate dates format

        return ResponseEntity.ok(orderService.findByDatesRange(startDate, endDate));
    }

    @GetMapping("/order/amount/{orderId}")
    public ResponseEntity getTotalAmount(@PathVariable("orderId") int orderId){
        try{
            Order order = orderService.findById(orderId);

            return ResponseEntity.ok(order.calculateTotalAmount());

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
