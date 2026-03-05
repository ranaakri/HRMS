package com.mycompany.hrms.data.dtos.travel.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TravelingUserReq {

    @NotNull
    private long userId;

    @NotNull
    @Min(0)
    private float travelBalance;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public float getTravelBalance() {
        return travelBalance;
    }

    public void setTravelBalance(float travelBalance) {
        this.travelBalance = travelBalance;
    }
}
