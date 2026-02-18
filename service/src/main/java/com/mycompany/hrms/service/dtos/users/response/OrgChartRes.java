package com.mycompany.hrms.service.dtos.users.response;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.service.dtos.travel.response.CreatedByUser;

public class OrgChartRes {
    private long userId;
    private String name;
    private String email;
    private String profileUrl;
    private Constants.Designation designation;
    private long assignedUnder;

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

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Constants.Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Constants.Designation designation) {
        this.designation = designation;
    }

    public long getAssignedUnder() {
        return assignedUnder;
    }

    public void setAssignedUnder(long assignedUnder) {
        this.assignedUnder = assignedUnder;
    }
}
