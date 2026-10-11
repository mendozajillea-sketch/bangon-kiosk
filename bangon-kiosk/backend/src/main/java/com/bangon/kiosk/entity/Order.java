package com.bangon.kiosk.entity;
import jakarta.persistence.*;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.*;
@Entity @Table(name="orders",indexes={@Index(name="idx_orders_customer",columnList="customer_id"),@Index(name="idx_orders_idempotency",columnList="idempotency_key",unique=true)}) public class Order {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true) public String orderNumber;
 @Column(name="idempotency_key",nullable=false,unique=true) public String idempotencyKey;
 @ManyToOne @JoinColumn(name="customer_id") public Customer customer;
 @Column(nullable=false) public String status="PENDING_PAYMENT",paymentMethod,paymentStatus="PENDING";
 @Column(nullable=false,precision=12,scale=2) public BigDecimal subtotal=BigDecimal.ZERO,discount=BigDecimal.ZERO,total=BigDecimal.ZERO;
 public int caffeine,calories,pointsEarned,pointsRedeemed;
 @Column(nullable=false,updatable=false) public Instant createdAt=Instant.now();
 @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER) public List<OrderItem> items=new ArrayList<>();
}
