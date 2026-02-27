package com.mycompany.hrms.service.users;

import com.mycompany.hrms.service.dtos.users.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    List<RoleResponse> getAllRoles();
}
