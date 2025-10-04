package com.eneifour.fantry.member.repository;

import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role findByRoleType(RoleType roleType);
}
