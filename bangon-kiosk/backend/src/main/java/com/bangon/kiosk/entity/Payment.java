package com.bangon.kiosk.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="payments") public class Payment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @OneToOne(optional=false) @JoinColumn(name="order_id",unique=true) public Order order;
 @Column(nullable=false) public String method,status="PENDING";
 public Instant confirmedAt;
}
