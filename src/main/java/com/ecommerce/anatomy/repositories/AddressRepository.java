package com.ecommerce.anatomy.repositories;

import com.ecommerce.anatomy.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {


    Optional<Address >findByAddressId(long addressId);
}
