package com.mycompany.hrms.data.entity.job;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "referralId")
@Entity

public class Referrals {

    public Referrals(){
        status = Constants.JobStatus.PENDING;
        createdAt = ZonedDateTime.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long referralId;

    @Column(nullable = false)
    private String candidateName;

    @Column(nullable = false, unique = true)
    private String candidateEmail;

    @Column(nullable = false)
    private String cvPath;

    @Column(nullable = false)
    private String referralNote;

    @Column(nullable = false)
    private Constants.JobStatus status;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private ZonedDateTime uploadedAt;

    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId")
    private Jobs job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referredBy")
    private Users referredBy;

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

    public ZonedDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(ZonedDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Jobs getJob() {
        return job;
    }

    public void setJob(Jobs job) {
        this.job = job;
    }

    public Users getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Users referredBy) {
        this.referredBy = referredBy;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
