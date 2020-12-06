package com.gallelloit.productcrud.controller;

import com.gallelloit.productcrud.model.Product;
import com.gallelloit.productcrud.service.ProductService;
import javassist.NotFoundException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductRestController {

    @Autowired
    ProductService productService;

    @PostMapping(path="/product", consumes = "application/json")
    public ResponseEntity insertProduct(@RequestBody Product product){

        product.setProductId(0);
        product.setIsActive(true);
        product.setCreateDate(LocalDateTime.now());

        try{
            productService.save(product);
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok(product);

    }

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> list;

        list = productService.findAll();

        return ResponseEntity.ok(list);
    }

    @PutMapping("/product")
    public ResponseEntity updateProduct(@RequestBody Product product){
        try{
            productService.save(product);
        }catch(NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity softDeleteProduct(@PathVariable("productId") int productId){
        try {
            productService.softRemove(productId);
        }catch(NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
