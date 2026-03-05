package com.mycompany.hrms.data.dtos.users.response;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.travel.response.CreatedByUser;

import java.time.ZonedDateTime;

public class UserProfileDto {

    private long userId;

    private String name;

    private String email;

    private Constants.Designation designation;

    private ZonedDateTime birthdate;

    private ZonedDateTime joiningDate;

    private String profileUrl;

    private boolean isActive;

    private RoleResponse role;

    private DepartmentResponse department;

    private CreatedByUser assignedUnder;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

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

    public ZonedDateTime getJoiningDate() {
        return joiningDate;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setBirthdate(ZonedDateTime birthdate) {
        this.birthdate = birthdate;
    }

    public void setJoiningDate(ZonedDateTime joiningDate) {
        this.joiningDate = joiningDate;
    }

    public RoleResponse getRole() {
        return role;
    }

    public void setRole(RoleResponse role) {
        this.role = role;
    }

    public DepartmentResponse getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponse department) {
        this.department = department;
    }

    public CreatedByUser getAssignedUnder() {
        return assignedUnder;
    }

    public void setAssignedUnder(CreatedByUser assignedUnder) {
        this.assignedUnder = assignedUnder;
    }
}
