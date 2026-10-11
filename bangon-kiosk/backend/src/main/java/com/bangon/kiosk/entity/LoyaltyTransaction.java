package com.bangon.kiosk.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="loyalty_transactions") public class LoyaltyTransaction {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public Customer customer;
 @ManyToOne(optional=false) public Order order;
 public int delta;
 @Column(nullable=false) public String reason;
 public Instant createdAt=Instant.now();
}
