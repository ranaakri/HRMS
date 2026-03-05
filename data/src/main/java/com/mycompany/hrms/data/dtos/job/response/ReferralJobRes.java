package com.mycompany.hrms.data.dtos.job.response;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.travel.response.CreatedByUser;


import java.time.ZonedDateTime;

public class ReferralJobRes {
    private long referralId;

    private String candidateName;

    private String candidateEmail;

    private String cvPath;

    private String referralNote;

    private Constants.JobStatus status;

    private ZonedDateTime createdAt;

    private CreatedByUser referredBy;

    private JobRes job;

    public JobRes getJob() {
        return job;
    }

    public void setJob(JobRes job) {
        this.job = job;
    }

    public long getReferralId() {
        return referralId;
    }

    public void setReferralId(long referralId) {
        this.referralId = referralId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public String getReferralNote() {
        return referralNote;
    }

    public void setReferralNote(String referralNote) {
        this.referralNote = referralNote;
    }

    public Constants.JobStatus getStatus() {
        return status;
    }

    public void setStatus(Constants.JobStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CreatedByUser getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(CreatedByUser referredBy) {
        this.referredBy = referredBy;
    }
}
