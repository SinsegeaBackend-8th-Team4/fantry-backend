package com.eneifour.fantry.member.service;

import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.exception.MemberErrorCode;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.member.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    //전체 권한 가져오기
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    //하나의 권한 가져오기
    public Role getRoleById(int id) {
        return roleRepository.findById(id).orElse(null);
    }

    //권한 추가하기
    @Transactional
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    //권한 수정하기
    @Transactional
    public Role updateRole(Role role) throws MemberException {
        if(!roleRepository.existsById(role.getRoleId())){
            throw new MemberException(MemberErrorCode.ROLE_NOT_FOUND);
        }
        return roleRepository.save(role);
    }

    //권한 삭제하기
    @Transactional
    public void deleteRole(int id) throws MemberException {
        if(!roleRepository.existsById(id)){
            throw new MemberException(MemberErrorCode.ROLE_NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }
}
