package com.mycompany.hrms.data.dtos.users.response;

public class DepartmentResponse {
    private long departmentId;
    private String name;

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
