package com.mycompany.hrms.data.entity.job;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "jobId")
@Entity
public class Jobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long jobId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String jobPost;

    @Column(nullable = false)
    private String jdFilePath;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Date lastApplicationDate;

    @Column(nullable = false)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hrContact")
    private Users hrContact;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "job")
    private List<JobShared> sharedTo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "job")
    private List<Referrals> referredTo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cv_reviewers",
            joinColumns = @JoinColumn(name = "jobId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<Users> cvReviewers;

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

    public String getJdFilePath() {
        return jdFilePath;
    }

    public void setJdFilePath(String jdFilePath) {
        this.jdFilePath = jdFilePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastApplicationDate() {
        return lastApplicationDate;
    }

    public void setLastApplicationDate(Date lastApplicationDate) {
        this.lastApplicationDate = lastApplicationDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Users getHrContact() {
        return hrContact;
    }

    public void setHrContact(Users hrContact) {
        this.hrContact = hrContact;
    }

    public List<JobShared> getSharedTo() {
        return sharedTo;
    }

    public void setSharedTo(List<JobShared> sharedTo) {
        this.sharedTo = sharedTo;
    }

    public List<Referrals> getReferredTo() {
        return referredTo;
    }

    public void setReferredTo(List<Referrals> referredTo) {
        this.referredTo = referredTo;
    }

    public Set<Users> getCvReviewers() {
        return cvReviewers;
    }

    public void setCvReviewers(Set<Users> cvReviewers) {
        this.cvReviewers = cvReviewers;
    }
}
