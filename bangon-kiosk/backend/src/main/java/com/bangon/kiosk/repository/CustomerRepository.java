package com.bangon.kiosk.repository;
import com.bangon.kiosk.entity.Customer;import org.springframework.data.jpa.repository.*;import jakarta.persistence.LockModeType;import java.util.*;
public interface CustomerRepository extends JpaRepository<Customer,Long>{Optional<Customer> findByEmailIgnoreCase(String email); @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select c from Customer c where c.id = :id") Optional<Customer> lockById(Long id);}
