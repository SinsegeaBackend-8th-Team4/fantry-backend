package com.eneifour.fantry.member.model;

import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.exception.MemberErrorCode;
import com.eneifour.fantry.member.exception.MemberException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final JpaRoleRepository jpaRoleRepository;

    //전체 권한 가져오기
    public List<Role> getRoles() {
        return jpaRoleRepository.findAll();
    }

    //하나의 권한 가져오기
    public Role getRoleById(int id) {
        return jpaRoleRepository.findById(id).orElse(null);
    }

    //권한 추가하기
    @Transactional
    public void saveRole(Role role) {
        jpaRoleRepository.save(role);
    }

    //권한 수정하기
    @Transactional
    public Role updateRole(Role role) throws MemberException {
        if(!jpaRoleRepository.existsById(role.getRoleId())){
            throw new MemberException(MemberErrorCode.ROLE_NOT_FOUND);
        }
        return jpaRoleRepository.save(role);
    }

    //권한 삭제하기
    @Transactional
    public void deleteRole(int id) throws MemberException {
        if(!jpaRoleRepository.existsById(id)){
            throw new MemberException(MemberErrorCode.ROLE_NOT_FOUND);
        }
        jpaRoleRepository.deleteById(id);
    }
}
