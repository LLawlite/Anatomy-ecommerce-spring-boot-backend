package com.ecommerce.anatomy.repositories;

import com.ecommerce.anatomy.model.AppRole;
import com.ecommerce.anatomy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
