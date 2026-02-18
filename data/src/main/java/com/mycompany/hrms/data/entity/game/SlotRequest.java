package com.mycompany.hrms.data.entity.game;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "requestId")
@Entity
public class SlotRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slotId", nullable = false)
    private GameSlots gameSlots;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestBy")
    private Users requestBy;

    @Column(nullable = false)
    private double groupAverageScore;

    @Column(nullable = false)
    private ZonedDateTime requestTimeStamp;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "request")
    private Set<RequestParticipants> participants;

    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

}
