package com.mycompany.hrms.data.dtos.travel.request;

import com.mycompany.hrms.data.constant.Constants;

public class UpdateExpenseStatus {
    private Constants.ExpenseStatus status;

    private String remarks;

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
