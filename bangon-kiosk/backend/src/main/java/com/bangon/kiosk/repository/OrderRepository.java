package com.bangon.kiosk.repository;
import com.bangon.kiosk.entity.Order;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;import org.springframework.data.jpa.repository.*;import jakarta.persistence.LockModeType;
public interface OrderRepository extends JpaRepository<Order,Long>{Optional<Order> findByIdempotencyKey(String key); @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select o from Order o where o.id = :id") Optional<Order> lockById(Long id); List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);}
