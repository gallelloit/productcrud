package com.gallelloit.productcrud.repository;

import com.gallelloit.productcrud.model.Product;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByNameIgnoreCase(String name);

    @Modifying
    @Query("UPDATE Product p SET p.isActive=false WHERE p.productId = :productId")
    @Transactional
    void softRemove(@Param("productId") int productId);
}
