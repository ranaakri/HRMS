package com.mycompany.hrms.data.entity.job;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Date;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "logId")
@Entity
public class JobShared {

    public JobShared(){
        status = Constants.JobStatus.PENDING;
        sharedAt = ZonedDateTime.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;

    @Column(nullable = false, unique = true)
    private String recipientEmail;

    @Column(nullable = false)
    private Constants.JobStatus status;

    @Column(nullable = false)
    private ZonedDateTime sharedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharedBy")
    private Users sharedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId")
    private Jobs job;

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

    public Users getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(Users sharedBy) {
        this.sharedBy = sharedBy;
    }

    public Jobs getJob() {
        return job;
    }

    public void setJob(Jobs job) {
        this.job = job;
    }
}
