package com.gallelloit.productcrud;

import com.gallelloit.productcrud.model.Product;
import com.gallelloit.productcrud.service.ProductService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    Product p1 = new Product("Pencil", 0.7, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p2 = new Product("Ball-pen", 1.2, LocalDateTime.of(2020,12,6,12,0,0), true);
    Product p3 = new Product("Wallet", 8.6, LocalDateTime.of(2020,12,6,12,0,0), true);

    @Test
    void insertsTwoProducts_thenReturnListWithNewProducts() throws NotFoundException {

        long count = productService.count();

        productService.save(p1);
        productService.save(p2);

        assertEquals(count+2, productService.count());
        assertTrue(productService.findAll().contains(p2));

        Product fetchProduct = productService.findByName("Ball-pen");
        assertEquals("Ball-pen", fetchProduct.getName());
    }

    @Test
    void insertsProduct_thenProductExists() throws NotFoundException {

        long count = productService.count();

        productService.save(p3);

        assertEquals(count+1, productService.count());

        Product fetchProduct = productService.findByName("Wallet");
        assertNotNull(fetchProduct);
        assertEquals("Wallet", fetchProduct.getName());

    }

    @Test
    void insertsProduct_thenFindsItById() throws NotFoundException {
        Product specialProduct = new Product("Purple Pencil", 3.2, LocalDateTime.of(2020,12,6,12,0,0), true);

        int productId = productService.save(specialProduct);

        assertEquals(specialProduct, productService.findById(productId));

    }

    @Test
    void searchNotExistingProduct_thenThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class,
                () -> productService.findById(Integer.MIN_VALUE));
    }

    @Test
    void updateExistingProduct_thenNameAndPriceChanged() throws NotFoundException {

        Product productToBeChanged = new Product("Name Before", 1, LocalDateTime.of(2020,12,6,12,0,0), true);
        int productId = productService.save(productToBeChanged);

        Product fetchProduct = productService.findById(productId);

        String newName = "Changed Name";
        double newPrice = 999999999999.99;

        fetchProduct.setName(newName);
        fetchProduct.setPrice(newPrice);

        productService.save(fetchProduct);

        Product mockProduct = new Product(productId, newName, newPrice, fetchProduct.getCreateDate(), true);

        assertEquals(mockProduct, productService.findByName("Changed Name"));

    }

    @Test
    void updatesNotExistingProduct_thenThrowsNotFoundException(){
        Product product = new Product(Integer.MIN_VALUE,"", 0, LocalDateTime.now(), true);
        Assertions.assertThrows(NotFoundException.class,
                () -> productService.save(product));
    }

    @Test
    void deleteExistingProduct_thenIfSearchesThrowsNotFoundException() throws NotFoundException {
        String productName = "Delete me";
        Product productToBeDeleted = new Product(productName, 1, LocalDateTime.of(2020,12,6,12,0,0), true);
        int productId = productService.save(productToBeDeleted);

        assertEquals(productToBeDeleted, productService.findByName(productName));
        productService.softRemove(productId);
        Assertions.assertThrows(NotFoundException.class,
                () -> productService.findById(productId));

    }

}
