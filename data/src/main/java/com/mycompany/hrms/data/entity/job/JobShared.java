package com.mycompany.hrms.data.entity.job;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.util.Date;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "logId")
@Entity
public class JobShared {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    @Column(nullable = false, unique = true)
    private String recipientEmail;

    @Column(nullable = false)
    private Date sharedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharedBy")
    private Users sharedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobId")
    private Jobs job;
}
