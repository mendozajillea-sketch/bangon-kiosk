package com.bangon.kiosk.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="order_items") public class OrderItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) @JoinColumn(name="order_id") public Order order;
 @ManyToOne(optional=false) @JoinColumn(name="product_id") public Product product;
 @Column(nullable=false) public String name;
 @Column(nullable=false,precision=10,scale=2) public BigDecimal unitPrice;
 public int quantity,caffeine,calories;
 public String size,milk; public int sweetness; public boolean extraShot,caramel,cream;
}
