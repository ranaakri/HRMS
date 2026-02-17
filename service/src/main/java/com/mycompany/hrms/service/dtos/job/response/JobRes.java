package com.mycompany.hrms.service.dtos.job.response;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.service.dtos.travel.response.CreatedByUser;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class JobRes {

    private long jobId;
    private String title;
    private String summary;
    private String jobPost;

    private String jdFilePath;

    private ZonedDateTime lastApplicationDate;

    private Constants.JobDataStatus status;
    private ZonedDateTime createdAt;

    private CreatedByUser hrContact;
    private Set<CreatedByUser> cvReviewers;

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getJobPost() {
        return jobPost;
    }

    public void setJobPost(String jobPost) {
        this.jobPost = jobPost;
    }

    public ZonedDateTime getLastApplicationDate() {
        return lastApplicationDate;
    }

    public void setLastApplicationDate(ZonedDateTime lastApplicationDate) {
        this.lastApplicationDate = lastApplicationDate;
    }

    public Constants.JobDataStatus getStatus() {
        return status;
    }

    public void setStatus(Constants.JobDataStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CreatedByUser getHrContact() {
        return hrContact;
    }

    public void setHrContact(CreatedByUser hrContact) {
        this.hrContact = hrContact;
    }

    public Set<CreatedByUser> getCvReviewers() {
        return cvReviewers;
    }

    public void setCvReviewers(Set<CreatedByUser> cvReviewers) {
        this.cvReviewers = cvReviewers;
    }

    public String getJdFilePath() {
        return jdFilePath;
    }

    public void setJdFilePath(String jdFilePath) {
        this.jdFilePath = jdFilePath;
    }
}
