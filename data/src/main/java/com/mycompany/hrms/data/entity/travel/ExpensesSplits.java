package com.mycompany.hrms.data.entity.travel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "splitId")
@Entity
public class ExpensesSplits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long splitId;

    @Column(nullable = false)
    private float splitAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseId")
    private Expenses expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelingUserId")
    private TravelingUser travelingUser;

    public long getSplitId() {
        return splitId;
    }

    public void setSplitId(long splitId) {
        this.splitId = splitId;
    }

    public float getSplitAmount() {
        return splitAmount;
    }

    public void setSplitAmount(float splitAmount) {
        this.splitAmount = splitAmount;
    }

    public Expenses getExpense() {
        return expense;
    }

    public void setExpense(Expenses expense) {
        this.expense = expense;
    }

    public TravelingUser getTravelingUser() {
        return travelingUser;
    }

    public void setTravelingUser(TravelingUser travelingUser) {
        this.travelingUser = travelingUser;
    }
}
