package com.bangon.kiosk.repository;
import com.bangon.kiosk.entity.Product;import org.springframework.data.jpa.repository.JpaRepository;import java.util.List;
public interface ProductRepository extends JpaRepository<Product,Long>{List<Product> findByActiveTrueOrderByIdAsc(); boolean existsByName(String name);}
