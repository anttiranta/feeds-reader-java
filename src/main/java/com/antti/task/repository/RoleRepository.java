package com.antti.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.antti.task.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
