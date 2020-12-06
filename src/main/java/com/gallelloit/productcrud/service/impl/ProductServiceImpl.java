package com.gallelloit.productcrud.service.impl;

import com.gallelloit.productcrud.model.Product;
import com.gallelloit.productcrud.repository.ProductRepository;
import com.gallelloit.productcrud.service.ProductService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public int save(Product product) throws NotFoundException {

        int productId = product.getProductId();

        if (productId != 0){
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if (!optionalProduct.isPresent()) {
                throw new NotFoundException(String.format("Product %d could not be found.", productId));
            }
        }

        Product savedProduct = productRepository.save(product);

        return savedProduct.getProductId();

    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByNameIgnoreCase(name);
    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public Product findById(int productId) throws NotFoundException {

        Optional<Product> optionalProduct= productRepository.findById(productId);

        if (!optionalProduct.isPresent()){
            throw new NotFoundException(String.format("Product %d could not be found.", productId));
        }

        return optionalProduct.get();
    }

    @Override
    public void softRemove(int productId) throws NotFoundException {
        Optional<Product> optionalProduct= productRepository.findById(productId);

        if (!optionalProduct.isPresent()){
            throw new NotFoundException(String.format("Product %d could not be found.", productId));
        }

        productRepository.softRemove(productId);
    }
}
