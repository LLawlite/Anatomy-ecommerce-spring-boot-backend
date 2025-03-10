package com.ecommerce.anatomy.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.anatomy.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}