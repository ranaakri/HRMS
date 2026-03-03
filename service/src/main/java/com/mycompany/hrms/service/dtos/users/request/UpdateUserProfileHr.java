package com.mycompany.hrms.service.dtos.users.request;

import com.mycompany.hrms.data.constant.Constants;

import java.time.ZonedDateTime;

public class UpdateUserProfileHr {
    private String name;
    private String email;
    private Constants.Designation designation;
    private ZonedDateTime birthdate;
    private ZonedDateTime joiningDate;
    private ZonedDateTime updatedAt;
    private long roleId;
    private long departmentId;
    private long assignedUnderId;

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

    public Constants.Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Constants.Designation designation) {
        this.designation = designation;
    }

    public ZonedDateTime getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(ZonedDateTime birthdate) {
        this.birthdate = birthdate;
    }

    public ZonedDateTime getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(ZonedDateTime joiningDate) {
        this.joiningDate = joiningDate;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getAssignedUnderId() {
        return assignedUnderId;
    }

    public void setAssignedUnderId(long assignedUnderId) {
        this.assignedUnderId = assignedUnderId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }
}
