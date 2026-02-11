package com.mycompany.hrms.service.dtos.travel.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddExpenseSplit {

    @Min(0)
    @NotNull
    private float splitAmount;

    @NotNull
    private long travelingUserId;

    public float getSplitAmount() {
        return splitAmount;
    }

    public void setSplitAmount(float splitAmount) {
        this.splitAmount = splitAmount;
    }

    public long getTravelingUserId() {
        return travelingUserId;
    }

    public void setTravelingUserId(long travelingUserId) {
        this.travelingUserId = travelingUserId;
    }
}
