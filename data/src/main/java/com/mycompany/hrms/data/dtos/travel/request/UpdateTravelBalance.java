package com.mycompany.hrms.data.dtos.travel.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateTravelBalance {

    @NotNull
    @Min(0)
    private float updatedBalance;

    public float getUpdatedBalance() {
        return updatedBalance;
    }

    public void setUpdatedBalance(float updatedBalance) {
        this.updatedBalance = updatedBalance;
    }
}
