package com.mycompany.hrms.service.users;

import com.mycompany.hrms.data.entity.user.Roles;
import com.mycompany.hrms.data.repository.users.RolesRepo;
import com.mycompany.hrms.data.dtos.users.response.RoleResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {

    private final RolesRepo rolesRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleService(RolesRepo rolesRepo,
                       ModelMapper modelMapper){
        this.rolesRepo = rolesRepo;
        this.modelMapper = modelMapper;
    }

    public List<RoleResponse> getAllRoles() {
        List<Roles> roles = rolesRepo.findAll();
        return roles.stream().map(val -> modelMapper.map(val, RoleResponse.class)).toList();
    }
}
