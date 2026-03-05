package com.mycompany.hrms.data.dtos.travel.request;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.DocResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.List;

public class AddExpense {

    public AddExpense(){
        this.expenseDate = ZonedDateTime.now();
    }

    @NotNull
    @Min(0)
    private float amount;

    @NotNull
    private Constants.Category category;

    @NotNull
    private ZonedDateTime expenseDate;

    @NotNull
    private long uploadedByUserId;

    @NotNull
    private long travelId;

    @NotNull
    private List<AddExpenseSplit> expensesSplits;

    @NotNull
    private List<DocResponse> expenseProof;

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

    public List<DocResponse> getExpenseProof() {
        return expenseProof;
    }

    public void setExpenseProof(List<DocResponse> expenseProof) {
        this.expenseProof = expenseProof;
    }
}
