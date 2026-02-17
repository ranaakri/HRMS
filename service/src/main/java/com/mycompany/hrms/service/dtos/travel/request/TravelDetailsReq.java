package com.mycompany.hrms.service.dtos.travel.request;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.service.dtos.DocResponse;
import jakarta.validation.constraints.*;

import java.time.ZonedDateTime;
import java.util.List;

public class TravelDetailsReq {

    @NotNull
    @NotEmpty
    @NotBlank
    private String title;

    @NotNull
    @Future
    private ZonedDateTime startDate;

    @NotNull
    @Future
    private ZonedDateTime endDate;

    @NotEmpty
    @NotBlank
    private String description;

    @NotNull
    private Constants.TravelStatus status;

    @Min(0)
    private float assignedBudget;

    @Min(0)
    private float totalExpense;

    @NotNull
    private long createdById;

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status.toString();
    }

    public void setStatus(Constants.TravelStatus status) {
        this.status = status;
    }

    public float getAssignedBudget() {
        return assignedBudget;
    }

    public void setAssignedBudget(float assignedBudget) {
        this.assignedBudget = assignedBudget;
    }

    public float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(float totalExpense) {
        this.totalExpense = totalExpense;
    }

    public long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(long createdBy) {
        this.createdById = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
