package com.mycompany.hrms.data.entity.travel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "travelingUserId")
@Entity
public class TravelingUser {

    public TravelingUser(){
        usedBalance = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long travelingUserId;

    @Column(nullable = false)
    private float travelBalance;

    @Column(nullable = false)
    private float usedBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelId")
    private TravelDetails travelDetails;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "travelingUser")
    private List<TravelDocuments> travelDocuments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "travelingUser")
    private List<ExpensesSplits> expensesSplits;

    public long getTravelingUserId() {
        return travelingUserId;
    }

    public void setTravelingUserId(long travelingUserId) {
        this.travelingUserId = travelingUserId;
    }

    public float getTravelBalance() {
        return travelBalance;
    }

    public void setTravelBalance(float travelBalance) {
        this.travelBalance = travelBalance;
    }

    public float getUsedBalance() {
        return usedBalance;
    }

    public void setUsedBalance(float usedBalance) {
        this.usedBalance = usedBalance;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public TravelDetails getTravelDetails() {
        return travelDetails;
    }

    public void setTravelDetails(TravelDetails travelDetails) {
        this.travelDetails = travelDetails;
    }

    public List<TravelDocuments> getTravelDocuments() {
        return travelDocuments;
    }

    public void setTravelDocuments(List<TravelDocuments> travelDocuments) {
        this.travelDocuments = travelDocuments;
    }

    public List<ExpensesSplits> getExpensesSplits() {
        return expensesSplits;
    }

    public void setExpensesSplits(List<ExpensesSplits> expensesSplits) {
        this.expensesSplits = expensesSplits;
    }
}