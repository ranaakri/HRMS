package com.mycompany.hrms.service.users;

import com.mycompany.hrms.service.dtos.users.DepartmentDto;
import java.util.List;

public interface IDepartmentService {
    DepartmentDto addDepartment(DepartmentDto department);
    DepartmentDto getDepartmentById(long departmentId);
    List<DepartmentDto> getAllDepartments();
}
