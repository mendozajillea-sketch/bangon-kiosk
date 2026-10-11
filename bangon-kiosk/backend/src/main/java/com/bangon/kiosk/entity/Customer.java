package com.bangon.kiosk.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="customers",indexes=@Index(name="idx_customers_email",columnList="email",unique=true)) public class Customer {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true,length=190) public String email;
 @Column(nullable=false) public String passwordHash;
 @Column(nullable=false) public String name;
 @Column(nullable=false) public String role="CUSTOMER";
 public int caffeineLimit=400,calorieGoal=2000,loyaltyPoints=0;
 public boolean lactoseIntolerant=false;
 @Version public long version;
 @Column(nullable=false,updatable=false) public Instant createdAt=Instant.now();
}
