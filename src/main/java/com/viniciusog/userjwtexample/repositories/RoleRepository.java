package com.viniciusog.userjwtexample.repositories;

import com.viniciusog.userjwtexample.model.Role;
import com.viniciusog.userjwtexample.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
