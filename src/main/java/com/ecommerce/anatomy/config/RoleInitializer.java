package com.ecommerce.anatomy.config;

import com.ecommerce.anatomy.model.AppRole;
import com.ecommerce.anatomy.model.Role;
import com.ecommerce.anatomy.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        addRoleIfNotExists(AppRole.ROLE_USER);
        addRoleIfNotExists(AppRole.ROLE_ADMIN);
        addRoleIfNotExists(AppRole.ROLE_SELLER);
    }

    private void addRoleIfNotExists(AppRole roleName) {
        Optional<Role> role = roleRepository.findByRoleName(roleName);
        if (role.isEmpty()) {
            roleRepository.save(new Role(roleName));
            System.out.println("✅ Created Role: " + roleName);
        }
    }
}
