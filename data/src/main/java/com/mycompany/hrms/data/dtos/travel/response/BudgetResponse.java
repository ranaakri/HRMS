package com.mycompany.hrms.data.dtos.travel.response;

public class BudgetResponse {
    private float travelBalance;

    private float usedBalance;

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
}
