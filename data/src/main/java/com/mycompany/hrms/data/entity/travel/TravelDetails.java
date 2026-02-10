package com.mycompany.hrms.data.entity.travel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "travelId")
@Entity
public class TravelDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long travelId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private ZonedDateTime startDate;

    @Column(nullable = false)
    private ZonedDateTime endDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Constants.TravelStatus status;

    @Column(nullable = false)
    private float assignedBudget;

    @Column(nullable = false)
    private float totalExpense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    private Users createdBy;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "travelDetails")
    private List<TravelGallery> travelGallery;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "travelDetails")
    private List<TravelPlan> travelPlans;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "travelDetails")
    private List<TravelingUser> travelingUsers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

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

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    public List<TravelGallery> getTravelGallery() {
        return travelGallery;
    }

    public void setTravelGallery(List<TravelGallery> travelGallery) {
        this.travelGallery = travelGallery;
    }

    public List<TravelPlan> getTravelPlans() {
        return travelPlans;
    }

    public void setTravelPlans(List<TravelPlan> travelPlans) {
        this.travelPlans = travelPlans;
    }

    public List<TravelingUser> getTravelingUsers() {
        return travelingUsers;
    }

    public void setTravelingUsers(List<TravelingUser> travelingUsers) {
        this.travelingUsers = travelingUsers;
    }
}
