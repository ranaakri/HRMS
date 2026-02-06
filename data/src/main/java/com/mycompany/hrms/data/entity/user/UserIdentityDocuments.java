package com.mycompany.hrms.data.entity.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.constant.Constants.DocType;
import jakarta.persistence.*;

import java.util.Date;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "identityDocId")
@Entity
public class UserIdentityDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int identityDocId;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DocType docType;

    @Column(nullable = false)
    private Date expiryDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private boolean isLocked;

    @Column(nullable = false)
    private Date uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;
}
