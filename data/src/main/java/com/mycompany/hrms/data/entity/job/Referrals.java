package com.mycompany.hrms.data.entity.job;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.util.Date;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "referralId")
@Entity

public class Referrals {

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
    private Date createdAt;

    @Column(nullable = false)
    private Date uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId")
    private Jobs job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referredBy")
    private Users referredBy;
}
