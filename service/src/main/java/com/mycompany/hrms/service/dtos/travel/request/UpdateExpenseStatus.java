package com.mycompany.hrms.service.dtos.travel.request;

import com.mycompany.hrms.data.constant.Constants;

public class UpdateExpenseStatus {
    private Constants.ExpenseStatus status;

    public Constants.ExpenseStatus getStatus() {
        return status;
    }

    public void setStatus(Constants.ExpenseStatus status) {
        this.status = status;
    }
}
