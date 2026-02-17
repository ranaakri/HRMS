package com.mycompany.hrms.service.dtos.travel.response;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.travel.ExpensesProofs;
import com.mycompany.hrms.service.dtos.travel.request.AddExpenseSplit;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class ExpenseRes {
    private long expenseId;

    private float amount;

    private Constants.Category category;

    private Date expenseDate;

    private long uploadedByUserId;

    private long travelId;

    private List<ExpenseSplitRes> expensesSplits;

    private Constants.ExpenseStatus status;

    private List<ExpenseProofRes> expensesProofs;

    private ZonedDateTime approvedAt;

    private String remarks;

    private CreatedByUser uploadedBy;

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public ZonedDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public CreatedByUser getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(CreatedByUser uploadedBy) {
        this.uploadedBy = uploadedBy;
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

    public long getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(long uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public List<ExpenseSplitRes> getExpensesSplits() {
        return expensesSplits;
    }

    public List<ExpenseProofRes> getExpensesProofs() {
        return expensesProofs;
    }

    public void setExpensesProofs(List<ExpenseProofRes> expensesProofs) {
        this.expensesProofs = expensesProofs;
    }

    public void setExpensesSplits(List<ExpenseSplitRes> expensesSplits) {
        this.expensesSplits = expensesSplits;
    }

    public Constants.ExpenseStatus getStatus() {
        return status;
    }

    public void setStatus(Constants.ExpenseStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
