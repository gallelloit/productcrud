package com.gallelloit.productcrud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    @Id
    @Column(name="order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name="products_orders",
            joinColumns=@JoinColumn(name="order_id"),
            inverseJoinColumns=@JoinColumn(name="product_id")
    )
    private List<Product> products;

    @Column(nullable = false)
    private String email;

    @Column(name="create_date", nullable = false)
    private LocalDateTime createDate;

    public double calculateTotalAmount(){
        return this.products.stream().mapToDouble(p->p.getPrice()).sum();
    }

    public Order(List<Product> products, String email, LocalDateTime createDate) {
        this.products = products;
        this.email = email;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderId == order.orderId &&
                products.equals(order.products) &&
                email.equals(order.email) &&
                createDate.equals(order.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, products, email, createDate);
    }
}
