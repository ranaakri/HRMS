package com.mycompany.hrms.service.dtos.users.request;

import com.mycompany.hrms.data.constant.Constants;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotEmpty;
//import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UserProfileCreate {
    private String name;
    private String email;
    private String password;
    private Constants.Designation designation;
    private Date joiningDate;
    private long departmentId;
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

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
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
}
