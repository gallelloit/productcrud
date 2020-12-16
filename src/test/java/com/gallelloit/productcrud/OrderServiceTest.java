package com.gallelloit.productcrud;

import com.gallelloit.productcrud.model.Order;
import com.gallelloit.productcrud.model.Product;
import com.gallelloit.productcrud.service.OrderService;
import com.gallelloit.productcrud.service.ProductService;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    Product p1 = new Product("Bloc", 0.7, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p2 = new Product("Mouse", 19.2, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p3 = new Product("Display", 180.6, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p4 = new Product("Table", 120.8, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p5 = new Product("Chair", 56.5, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p6 = new Product("Printer", 200, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p7 = new Product("Laptop", 1730.23, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p8 = new Product("Headphones", 65, LocalDateTime.of(2020,12,6,12,0,0), true);

    List<Product> products;

    @Test
    void placeOrderWithNewProduct_thenOrderExists() throws Exception {

        long orderCount = orderService.count();

        List<Product> productList = new ArrayList<>();
        productList.add(p1);

        String email = "user@shop.com";
        Order order = new Order(productList, email, LocalDateTime.of(2020,12,6,12,0,0));

        int orderId = orderService.save(order);

        assertEquals(orderCount+1, orderService.count());

        Order fetchOrder = orderService.findById(orderId);

        assertEquals(1, fetchOrder.getProducts().size());
        assertTrue(fetchOrder.getProducts().contains(p1));
        assertEquals(email, fetchOrder.getEmail());

    }

    @Test
    void placeOrderWithExistingProduct_thenOrderExists() throws Exception {
        // Place an order with one or more existing products. The order is inserted. The productos don't
        // get updated.

        long count = productService.count();

        productService.save(p1);
        productService.save(p2);

        assertEquals(count+2, productService.count());
        assertTrue(productService.findAll().contains(p2));
        List<Product> l = new ArrayList<>();
        p2.setName("Other name");
        l.add(p2);

        Order order = new Order(l, "user@shop.com", LocalDateTime.of(2020,12,6,12,0,0));

        int orderId = orderService.save(order);

        Order fetchOrder = orderService.findById(orderId);

        assertEquals(orderId, fetchOrder.getOrderId());
        assertEquals(1, fetchOrder.getProducts().size());
        assertEquals(p2.getProductId(), fetchOrder.getProducts().get(0).getProductId());
        assertEquals("Mouse", fetchOrder.getProducts().get(0).getName());

    }

    @Test
    void placeOrderWithProductWithWrongId_thenCreatesNewProductAndNewOrder() throws Exception {
        long count = productService.count();

        productService.save(p3);

        assertEquals(count+1, productService.count());
        assertTrue(productService.findAll().contains(p3));
        List<Product> l = new ArrayList<>();
        p3.setProductId(Integer.MIN_VALUE);
        l.add(p3);

        Order order = new Order(l, "user@shop.com", LocalDateTime.of(2020,12,6,12,0,0));

        int orderId = orderService.save(order);

        Order fetchOrder = orderService.findById(orderId);

        assertEquals(orderId, fetchOrder.getOrderId());
        assertEquals(1, fetchOrder.getProducts().size());
        assertNotEquals(Integer.MIN_VALUE, fetchOrder.getProducts().get(0).getProductId());
        assertEquals("Display", fetchOrder.getProducts().get(0).getName());

    }



    @Test
    void placeOrderWithoutProducts_thenThrowsException(){
        Order order = new Order(new ArrayList<>(), "user@shop.com", LocalDateTime.of(2020,12,6,12,0,0));

        Assertions.assertThrows(Exception.class,
                () -> orderService.save(order));

    }

    @Test
    void searchByDateNotExistingOrders_thenDoesNotReturnAnyOrder() throws Exception {
        long orderCount = orderService.count();
        LocalDateTime startDate = LocalDateTime.of(1900,1,1,0,0,0);
        LocalDateTime endDate = LocalDateTime.of(1901,1,1,0,0,0);

        List<Product> productList = new ArrayList<>();
        productList.add(p2);

        String email = "user@shop.com";
        Order order = new Order(productList, email, LocalDateTime.of(2020,12,6,12,0,0));

        int orderId = orderService.save(order);
        assertEquals(orderCount+1, orderService.count());

        List<Order> fetchOrders = orderService.findByDatesRange(startDate, endDate);

        assertEquals(0,fetchOrders.size());

    }

    @Test
    void searchByDateExistingOrders_thenReturnsOrders() throws Exception {
        long orderCount = orderService.count();
        LocalDateTime startDate = LocalDateTime.of(2005,1,1,0,0,0);
        LocalDateTime endDate = LocalDateTime.of(2007,1,1,0,0,0);

        List<Product> productList1 = new ArrayList<>();
        productList1.add(p3);

        List<Product> productList2 = new ArrayList<>();
        productList2.add(p4);
        productList2.add(p5);

        String email = "user@shop.com";
        Order order1 = new Order(productList1, email, LocalDateTime.of(2006,12,6,12,0,0));
        Order order2 = new Order(productList2, email, LocalDateTime.of(1999,12,6,12,0,0));

        orderService.save(order1);
        orderService.save(order2);

        assertEquals(orderCount+2, orderService.count());

        List<Order> fetchOrders = orderService.findByDatesRange(startDate, endDate);

        assertEquals(1, fetchOrders.size());
        assertEquals(2006, fetchOrders.get(0).getCreateDate().getYear());

    }

    @Test
    void calculatesAmountOfOrderWithSingleProduct_thenReturnsCorrectAmount() throws Exception {
        long orderCount = orderService.count();

        List<Product> productList = new ArrayList<>();
        productList.add(p6);

        String email = "user@shop.com";
        Order order = new Order(productList, email, LocalDateTime.of(2020,12,6,12,0,0));

        orderService.save(order);

        assertEquals(orderCount+1, orderService.count());

        assertEquals(200, order.calculateTotalAmount());

    }

    @Test
    void calculatesAmountOfOrderWithMoreThanOneProduct_thenReturnsCorrectAmount() throws Exception {
        long orderCount = orderService.count();

        List<Product> productList = new ArrayList<>();
        productList.add(p7);
        productList.add(p8);

        String email = "user@shop.com";
        Order order = new Order(productList, email, LocalDateTime.of(2020,12,6,12,0,0));

        orderService.save(order);

        assertEquals(orderCount+1, orderService.count());

        assertEquals(1795.23, order.calculateTotalAmount());

    }
}
