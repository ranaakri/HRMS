package com.mycompany.hrms.api.controllers.users;

import com.mycompany.hrms.service.dtos.users.response.RoleResponse;
import com.mycompany.hrms.service.users.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final IRoleService roleService;

    @Autowired
    public RoleController(IRoleService roleService){
        this.roleService = roleService;
    }

    @Operation(
            summary = "Get list of roles"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @GetMapping("/list")
    public ResponseEntity<List<RoleResponse>> getAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
