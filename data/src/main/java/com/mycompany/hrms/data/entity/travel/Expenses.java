package com.mycompany.hrms.data.entity.travel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "expenseId")
@Entity
public class Expenses {

    public Expenses(){
        status = Constants.ExpenseStatus.PENDING;
        remarks = "Expense Added";
        expensesProofs = null;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long expenseId;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private Constants.Category category;

    @Column(nullable = false)
    private ZonedDateTime expenseDate;

    @Column(nullable = false)
    private String remarks;

    @Column(nullable = false)
    private Constants.ExpenseStatus status;

    @Column(nullable = true)
    private ZonedDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploadedBy")
    private Users uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelId")
    private TravelDetails travelDetails;

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

    public ZonedDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(ZonedDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status.toString();
    }

    public void setStatus(Constants.ExpenseStatus status) {
        this.status = status;
    }

    public ZonedDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
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

    public Users getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Users uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public TravelDetails getTravelDetails() {
        return travelDetails;
    }

    public void setTravelDetails(TravelDetails travelDetails) {
        this.travelDetails = travelDetails;
    }
}