package com.eneifour.fantry.member.model;

import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends JpaRepository<Role, Integer> {
    public Role findByRoleType(RoleType roleType);
}
