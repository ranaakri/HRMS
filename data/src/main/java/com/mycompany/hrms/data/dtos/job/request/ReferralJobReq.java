package com.mycompany.hrms.data.dtos.job.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public class ReferralJobReq {

    @NotNull
    @NotEmpty
    @NotBlank
    private String candidateName;

    @Email
    @NotNull
    private String candidateEmail;

    @NotNull
    @NotBlank
    private String cvPath;

    @NotNull
    @NotBlank
    @NotEmpty
    private String publicId;

    private String referralNote;

    private ZonedDateTime uploadedAt;

    @NotNull
    private long jobId;

    @NotNull
    private long referredById;

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

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getReferredById() {
        return referredById;
    }

    public void setReferredById(long referredById) {
        this.referredById = referredById;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public ZonedDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(ZonedDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
