package com.mycompany.hrms.service.users;

import com.mycompany.hrms.data.entity.user.Departments;
import com.mycompany.hrms.data.repository.users.DepartmentsRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.users.DepartmentDto;
import com.mycompany.hrms.data.dtos.users.request.AddDepartmentDto;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentsService implements IDepartmentService{

    private final DepartmentsRepo departmentsRepo;
    private final ModelMapper modelMapper;
    private final UsersRepo usersRepo;

    @Autowired
    public DepartmentsService(ModelMapper modelMapper, DepartmentsRepo departmentsRepo, UsersRepo usersRepo){
        this.departmentsRepo = departmentsRepo;
        this.modelMapper = modelMapper;
        this.usersRepo = usersRepo;
    }

    public DepartmentDto addDepartment(AddDepartmentDto department){
        Departments data = modelMapper.map(department, Departments.class);
        return modelMapper.map(departmentsRepo.save(data), DepartmentDto.class);
    }

    public DepartmentDto getDepartmentById(long departmentId){
        Departments department = departmentsRepo.findById(departmentId)
                .orElseThrow( () -> new ResourceNotFoundException("Department not found"));
        return modelMapper.map(department, DepartmentDto.class);
    }

    public List<DepartmentDto> getAllDepartments(){
        List<Departments> departments = departmentsRepo.findAll();
        return departments.stream().map(data -> modelMapper.map(data, DepartmentDto.class)).toList();
    }

    public DepartmentDto updateDepartment(long id, DepartmentDto updated){
        Departments dept = departmentsRepo.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Department not found"));
        dept.setName(updated.getName());
        dept.setDescription(updated.getDescription());

        return modelMapper.map(departmentsRepo.save(dept), DepartmentDto.class);
    }

    public void deleteDepartment(long departmentId){
        if(!departmentsRepo.existsById(departmentId)){
            throw new ResourceNotFoundException("Department not found");
        }
        if(usersRepo.existsByDepartment_DepartmentId(departmentId)){
            throw new BadRequestException("Some user currently have this department, please remove them first");
        }
        departmentsRepo.deleteById(departmentId);
    }
}