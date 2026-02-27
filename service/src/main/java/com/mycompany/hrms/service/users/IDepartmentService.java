package com.mycompany.hrms.service.users;

import com.mycompany.hrms.service.dtos.users.DepartmentDto;
import com.mycompany.hrms.service.dtos.users.request.AddDepartmentDto;

import java.util.List;

public interface IDepartmentService {
    DepartmentDto addDepartment(AddDepartmentDto department);
    DepartmentDto getDepartmentById(long departmentId);
    List<DepartmentDto> getAllDepartments();
}
