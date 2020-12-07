package com.gallelloit.productcrud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Where(clause="is_active=1")
public class Product {

    @Id
    @Column(name="product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;


    @Column(name="create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name="is_active", nullable = false)
    private Boolean isActive;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name="products_orders",
//            joinColumns=@JoinColumn(name="product_id"),
//            inverseJoinColumns=@JoinColumn(name="order_id")
//    )
//    private List<Order> orders;

    public Product(String name, double price, LocalDateTime createDate, boolean active) {
        this.name = name;
        this.price = price;
        this.createDate = createDate;
        this.isActive = active;
        //this.orders = new ArrayList<>();
    }
//
//    public Product(int productId, String name, double price, LocalDateTime createDate, Boolean isActive) {
//        this.productId = productId;
//        this.name = name;
//        this.price = price;
//        this.createDate = createDate;
//        this.isActive = isActive;
//        //this.orders = new ArrayList<>();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return productId == product.productId &&
                Double.compare(product.price, price) == 0 &&
                name.equals(product.name) &&
                createDate.equals(product.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, price, createDate);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", createDate=" + createDate +
                '}';
    }
}
