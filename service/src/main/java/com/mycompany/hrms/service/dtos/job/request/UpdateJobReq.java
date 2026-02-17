package com.mycompany.hrms.service.dtos.job.request;

import com.mycompany.hrms.data.constant.Constants;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.List;

public class UpdateJobReq {

        @NotNull
        @NotEmpty
        @NotBlank
        private String title;

        @NotNull
        @NotEmpty
        @NotBlank
        private String summary;

        @NotNull
        @NotEmpty
        @NotBlank
        private String jobPost;

        @Future
        private ZonedDateTime lastApplicationDate;

        @NotNull
        private Constants.JobDataStatus status;

        @NotNull
        private long hrId;

        private List<Long> cvReviewersList;

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

    public long getHrId() {
        return hrId;
    }

    public void setHrId(long hrId) {
        this.hrId = hrId;
    }

    public List<Long> getCvReviewersList() {
        return cvReviewersList;
    }

    public void setCvReviewersList(List<Long> cvReviewersList) {
        this.cvReviewersList = cvReviewersList;
    }
}
