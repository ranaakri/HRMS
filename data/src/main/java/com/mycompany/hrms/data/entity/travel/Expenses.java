package com.mycompany.hrms.data.entity.travel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants;
import jakarta.persistence.*;

import java.util.Date;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "expenseId")
@Entity
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int expenseId;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private Constants.Category category;

    @Column(nullable = false)
    private Date expenseDate;

    @Column(nullable = false)
    private String remarks;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String proofFilePath;

    @Column(nullable = false)
    private Date approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelingUserId")
    private TravelingUser travelingUser;
}
