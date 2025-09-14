package com.ecommerce.project.repository;

import com.ecommerce.project.entity.Payment;
import com.ecommerce.project.enumpackage.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderCustomerIdAndStatus(Long customerId, PaymentStatus status);
}
