package com.ecommerce.anatomy.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.anatomy.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}