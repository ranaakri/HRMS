package com.mycompany.hrms.service.dtos.job;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class CreateJobReq {

    private String title;

    private String summary;

    private String jobPost;

    private ZonedDateTime lastApplicationDate;

    private Constants.JobDataStatus status;

    private ZonedDateTime createdAt;

    private long hrId;

    private List<Long> cvReviewerId;
}
