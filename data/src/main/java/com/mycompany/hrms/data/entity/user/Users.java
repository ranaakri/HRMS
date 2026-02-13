package com.mycompany.hrms.data.entity.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.job.JobShared;
import com.mycompany.hrms.data.entity.job.Jobs;
import com.mycompany.hrms.data.entity.job.Referrals;
import com.mycompany.hrms.data.entity.notification.NotificationReceivers;
import com.mycompany.hrms.data.entity.travel.*;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Constants.Designation designation;

    @Column(nullable = true)
    private ZonedDateTime birthdate;

    @Column(nullable = false)
    private ZonedDateTime joiningDate;

    @Column(nullable = true)
    private String profileUrl;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    public Users(){
        updatedAt = ZonedDateTime.now();
        createdAt = ZonedDateTime.now();
    }

    //User

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedUnder")
    private Users assignedUnder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private Departments department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private Roles role;

    //Travel

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserIdentityDocuments> userIdentityDocuments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<TravelDetails> travelDetails;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<TravelingUser> travelList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "uploadedBy")
    private List<TravelDocuments> travelDocuments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "uploadedBy")
    private List<Expenses> expenses;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<NotificationReceivers> notifications;

    //Jobs

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "hrContact")
    private List<Jobs> hrInJobs;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sharedBy")
    private List<JobShared> jobShared;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "referredBy")
    private List<Referrals> referrals;

    @ManyToMany(mappedBy = "cvReviewers")
    private Set<Jobs> jobsCvReviewer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public void setJoiningDate(ZonedDateTime joiningDate) {
        this.joiningDate = joiningDate;
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

    public Users getAssignedUnder() {
        return assignedUnder;
    }

    public void setAssignedUnder(Users assignedUnder) {
        this.assignedUnder = assignedUnder;
    }

    public Departments getDepartment() {
        return department;
    }

    public void setDepartment(Departments department) {
        this.department = department;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public List<UserIdentityDocuments> getUserIdentityDocuments() {
        return userIdentityDocuments;
    }

    public void setUserIdentityDocuments(List<UserIdentityDocuments> userIdentityDocuments) {
        this.userIdentityDocuments = userIdentityDocuments;
    }

    public List<TravelDetails> getTravelDetails() {
        return travelDetails;
    }

    public void setTravelDetails(List<TravelDetails> travelDetails) {
        this.travelDetails = travelDetails;
    }

    public List<TravelingUser> getTravelList() {
        return travelList;
    }

    public void setTravelList(List<TravelingUser> travelList) {
        this.travelList = travelList;
    }

    public List<TravelDocuments> getTravelDocuments() {
        return travelDocuments;
    }

    public void setTravelDocuments(List<TravelDocuments> travelDocuments) {
        this.travelDocuments = travelDocuments;
    }

    public List<Jobs> getHrInJobs() {
        return hrInJobs;
    }

    public void setHrInJobs(List<Jobs> hrInJobs) {
        this.hrInJobs = hrInJobs;
    }

    public List<JobShared> getJobShared() {
        return jobShared;
    }

    public void setJobShared(List<JobShared> jobShared) {
        this.jobShared = jobShared;
    }

    public List<Referrals> getReferrals() {
        return referrals;
    }

    public void setReferrals(List<Referrals> referrals) {
        this.referrals = referrals;
    }

    public Set<Jobs> getJobsCvReviewer() {
        return jobsCvReviewer;
    }

    public void setJobsCvReviewer(Set<Jobs> jobsCvReviewer) {
        this.jobsCvReviewer = jobsCvReviewer;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Expenses> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expenses> expenses) {
        this.expenses = expenses;
    }

    public List<NotificationReceivers> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationReceivers> notifications) {
        this.notifications = notifications;
    }

    public ZonedDateTime getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(ZonedDateTime birthdate) {
        this.birthdate = birthdate;
    }
}
