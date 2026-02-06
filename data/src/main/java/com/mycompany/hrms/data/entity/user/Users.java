package com.mycompany.hrms.data.entity.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.job.JobShared;
import com.mycompany.hrms.data.entity.job.Jobs;
import com.mycompany.hrms.data.entity.job.Referrals;
import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelDocuments;
import com.mycompany.hrms.data.entity.travel.TravelingUser;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "employeeId")
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private Date joiningDate;

    @Column(nullable = true)
    private String profileUrl;

    @Column(nullable = false)
    private boolean isActive;

    //User

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedUnder")
    private Users assignedUnder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private Departments department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private Roles role;

    //Travel

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserIdentityDocuments> userIdentityDocuments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<TravelDetails> travelDetails;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<TravelingUser> travelList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "uploadedBy")
    private List<TravelDocuments> travelDocuments;

    //Jobs

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "hrContact")
    private List<Jobs> hrInJobs;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sharedBy")
    private List<JobShared> jobShared;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "referredBy")
    private List<Referrals> referrals;

    @ManyToMany(mappedBy = "cvReviewers")
    private Set<Jobs> jobsCvReviewer;
}
