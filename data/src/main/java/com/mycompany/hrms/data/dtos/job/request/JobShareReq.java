package com.mycompany.hrms.data.dtos.job.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class JobShareReq {

    @NotNull
    @Email
    private String recipientEmail;

    @NotNull
    private long sharedBy;

    @NotNull
    private long jobId;

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public long getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(long sharedBy) {
        this.sharedBy = sharedBy;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }
}
