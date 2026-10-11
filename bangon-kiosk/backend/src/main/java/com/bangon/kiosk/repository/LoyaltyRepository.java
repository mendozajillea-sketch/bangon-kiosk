package com.bangon.kiosk.repository;
import com.bangon.kiosk.entity.LoyaltyTransaction;import org.springframework.data.jpa.repository.JpaRepository;
public interface LoyaltyRepository extends JpaRepository<LoyaltyTransaction,Long>{}
