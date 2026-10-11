package com.bangon.kiosk.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="products", indexes=@Index(name="idx_product_name",columnList="name")) public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) @JoinColumn(name="category_id") public Category category;
 @Column(nullable=false) public String name;
 @Column(nullable=false,precision=10,scale=2) public BigDecimal price;
 @Column(nullable=false) public int caffeine,calories;
 @Column(length=1024) public String image;
 public boolean pastry,active=true;
 public Product(){}
 public Product(Category category,String name,int price,int caffeine,int calories,String image,boolean pastry){this.category=category;this.name=name;this.price=BigDecimal.valueOf(price);this.caffeine=caffeine;this.calories=calories;this.image=image;this.pastry=pastry;}
}
