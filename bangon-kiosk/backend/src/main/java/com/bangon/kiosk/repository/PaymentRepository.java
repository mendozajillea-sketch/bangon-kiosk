package com.bangon.kiosk.repository;
import com.bangon.kiosk.entity.Payment;import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment,Long>{Payment findByOrderId(Long id);}
