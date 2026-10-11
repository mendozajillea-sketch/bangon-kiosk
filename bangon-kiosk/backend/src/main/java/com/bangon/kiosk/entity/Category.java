package com.bangon.kiosk.entity;
import jakarta.persistence.*;
@Entity @Table(name="categories") public class Category {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true,length=70) public String name;
 public Category(){} public Category(String n){name=n;}
}
