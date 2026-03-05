package com.mycompany.hrms.data.dtos.users.request;

import com.mycompany.hrms.data.constant.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.aspectj.lang.annotation.Before;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotEmpty;
//import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.Date;

public class UserProfileCreate {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String password;
    private Constants.Designation designation;
    private ZonedDateTime joiningDate;

    private ZonedDateTime birthdate;

    private long assignUnderId;

    @NotNull
    private long departmentId;

    @NotNull
    private long roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Constants.Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Constants.Designation designation) {
        this.designation = designation;
    }

    public ZonedDateTime getJoiningDate() {
        return joiningDate;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public void setJoiningDate(ZonedDateTime joiningDate) {
        this.joiningDate = joiningDate;
    }

    public ZonedDateTime getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(ZonedDateTime birthdate) {
        this.birthdate = birthdate;
    }

    public long getAssignUnderId() {
        return assignUnderId;
    }

    public void setAssignUnderId(long assignUnderId) {
        this.assignUnderId = assignUnderId;
    }
}
