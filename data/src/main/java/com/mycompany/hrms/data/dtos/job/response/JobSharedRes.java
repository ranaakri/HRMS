package com.mycompany.hrms.data.dtos.job.response;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.travel.response.CreatedByUser;

import java.time.ZonedDateTime;

public class JobSharedRes {
    private long logId;
    private String recipientEmail;
    private Constants.JobStatus status;
    private ZonedDateTime sharedAt;
    private CreatedByUser sharedBy;

    private JobRes job;

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public Constants.JobStatus getStatus() {
        return status;
    }

    public void setStatus(Constants.JobStatus status) {
        this.status = status;
    }

    public ZonedDateTime getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(ZonedDateTime sharedAt) {
        this.sharedAt = sharedAt;
    }

    public CreatedByUser getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(CreatedByUser sharedBy) {
        this.sharedBy = sharedBy;
    }

    public JobRes getJob() {
        return job;
    }

    public void setJob(JobRes job) {
        this.job = job;
    }
}
