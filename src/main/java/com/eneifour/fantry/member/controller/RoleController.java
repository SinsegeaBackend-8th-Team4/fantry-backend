package com.eneifour.fantry.member.controller;

import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.model.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoleController {
    private final RoleService roleService;

    //권한 전체 가져오기
    @GetMapping("/role")
    public ResponseEntity<?> getRole() {
        List<Role> roleList = roleService.getRoles();
        return ResponseEntity.ok().body(Map.of("roleList", roleList));
    }

    //권한 하나 가져오기
    @GetMapping("/role/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable int id) {
        Role role = roleService.getRoleById(id);
        return  ResponseEntity.ok().body(Map.of("role", role));
    }

    //신규 권한 추가하기
    @PostMapping("/role")
    public ResponseEntity<?> addRole(@RequestBody Role role) {
        roleService.saveRole(role);
        return ResponseEntity.ok().body(Map.of("result", "권한이 성공적으로 등록됨"));
    }

    //권한 수정하기
    @PutMapping("/role/{id}")
    public ResponseEntity<?> updateRole(@PathVariable int id, @RequestBody Role role) {
        roleService.updateRole(role);
        return ResponseEntity.ok().body(Map.of("result", "권한이 성공적으로 수정됨"));
    }

    //권한 삭제하기
    @DeleteMapping("/role/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().body(Map.of("result", "권한이 성공적으로 삭제됨"));
    }
}
