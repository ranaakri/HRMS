package com.mycompany.hrms.data.entity.travel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "expenseId")
@Entity
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long expenseId;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private Constants.Category category;

    @Column(nullable = false)
    private Date expenseDate;

    @Column(nullable = false)
    private String remarks;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Date approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelingUserId")
    private TravelingUser travelingUser;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "expenses")
    private List<ExpensesProofs> expensesProofs;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "expense")
    private List<ExpensesSplits> expensesSplits;

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Constants.Category getCategory() {
        return category;
    }

    public void setCategory(Constants.Category category) {
        this.category = category;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    public TravelingUser getTravelingUser() {
        return travelingUser;
    }

    public void setTravelingUser(TravelingUser travelingUser) {
        this.travelingUser = travelingUser;
    }

    public List<ExpensesProofs> getExpensesProofs() {
        return expensesProofs;
    }

    public void setExpensesProofs(List<ExpensesProofs> expensesProofs) {
        this.expensesProofs = expensesProofs;
    }

    public List<ExpensesSplits> getExpensesSplits() {
        return expensesSplits;
    }

    public void setExpensesSplits(List<ExpensesSplits> expensesSplits) {
        this.expensesSplits = expensesSplits;
    }
}