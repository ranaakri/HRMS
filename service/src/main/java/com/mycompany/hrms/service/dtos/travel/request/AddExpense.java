package com.mycompany.hrms.service.dtos.travel.request;

import com.mycompany.hrms.data.constant.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public class AddExpense {

    @NotNull
    @Min(0)
    private float amount;

    @NotNull
    private Constants.Category category;

    @NotNull
    private Date expenseDate;

    @NotNull
    private long uploadedByUserId;

    @NotNull
    private long travelId;

    @NotNull
    private List<AddExpenseSplit> expensesSplits;

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

    public List<AddExpenseSplit> getExpensesSplits() {
        return expensesSplits;
    }

    public void setExpensesSplits(List<AddExpenseSplit> expensesSplits) {
        this.expensesSplits = expensesSplits;
    }
}
