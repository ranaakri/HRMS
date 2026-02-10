package com.mycompany.hrms.service.dtos.travel.response;

public class TravelingUserRes {
    private long travelingUserId;

    private float travelBalance;

    private float usedBalance;

    private CreatedByUser user; //Reused Created by user

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

    public CreatedByUser getUser() {
        return user;
    }

    public void setUser(CreatedByUser user) {
        this.user = user;
    }
}
