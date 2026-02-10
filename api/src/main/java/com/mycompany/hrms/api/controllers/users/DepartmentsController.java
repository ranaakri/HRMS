package com.mycompany.hrms.api.controllers.users;

import com.mycompany.hrms.service.dtos.users.DepartmentDto;
import com.mycompany.hrms.service.users.DepartmentsService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    public DepartmentsController(DepartmentsService departmentsService){
        this.departmentsService = departmentsService;
    }

    @Operation(
            summary = "Get all department",
            description = "Get list of all departments in the system"
    )
    @GetMapping(value = "/list")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments(){
        return ResponseEntity.ok(departmentsService.getAllDepartments());
    }

    @Operation(
            summary = "Get department by id",
            description = "Get department name and description by department id"
    )
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long departmentId){
        return ResponseEntity.ok(departmentsService.getDepartmentById(departmentId));
    }

    @Operation(
            summary = "Add new Department",
            description = "Add new department into the database"
    )
    @PostMapping("/")
    public ResponseEntity<DepartmentDto> addDepartment(@RequestBody DepartmentDto department){
        return new ResponseEntity<>(departmentsService.addDepartment(department), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update Department",
            description = "Update department name and description"
    )
    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable long departmentId, @RequestBody DepartmentDto updated){
        return new ResponseEntity<>(departmentsService.updateDepartment(departmentId,updated), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete department by id",
            description = "Delete department by department id"
    )
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable long departmentId){
        departmentsService.deleteDepartment(departmentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
