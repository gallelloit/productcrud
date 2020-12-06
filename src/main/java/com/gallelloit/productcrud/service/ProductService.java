package com.gallelloit.productcrud.service;

import com.gallelloit.productcrud.model.Product;
import javassist.NotFoundException;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.List;

public interface ProductService {

    int save(Product product) throws NotFoundException;
    List<Product> findAll();

    Product findByName(String name);
    long count();

    Product findById(int productId) throws NotFoundException;

    void softRemove(int productId) throws NotFoundException;
}
