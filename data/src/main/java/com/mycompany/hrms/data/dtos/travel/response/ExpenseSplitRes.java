package com.mycompany.hrms.data.dtos.travel.response;

import com.mycompany.hrms.data.dtos.travel.request.AddExpenseSplit;

public class ExpenseSplitRes extends AddExpenseSplit {
    private long splitId;

    private float splitAmount;

    private TravelingUserRes travelingUser;

    public long getSplitId() {
        return splitId;
    }

    public void setSplitId(long splitId) {
        this.splitId = splitId;
    }

    @Override
    public float getSplitAmount() {
        return splitAmount;
    }

    @Override
    public void setSplitAmount(float splitAmount) {
        this.splitAmount = splitAmount;
    }

    public TravelingUserRes getTravelingUser() {
        return travelingUser;
    }

    public void setTravelingUser(TravelingUserRes travelingUser) {
        this.travelingUser = travelingUser;
    }
}
