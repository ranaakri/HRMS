package com.mycompany.hrms.data.entity.game;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.HashSet;
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
    private Set<RequestParticipants> participants = new HashSet<>();

    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED,
        DELETED
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public GameSlots getGameSlots() {
        return gameSlots;
    }

    public void setGameSlots(GameSlots gameSlots) {
        this.gameSlots = gameSlots;
    }

    public Users getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Users requestBy) {
        this.requestBy = requestBy;
    }

    public double getGroupAverageScore() {
        return groupAverageScore;
    }

    public void setGroupAverageScore(double groupAverageScore) {
        this.groupAverageScore = groupAverageScore;
    }

    public ZonedDateTime getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public void setRequestTimeStamp(ZonedDateTime requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Set<RequestParticipants> getParticipants() {
        return participants;
    }

    public void addRequestParticipants(RequestParticipants r){
        this.participants.add(r);
        r.setRequest(this);
    }

    public void setParticipants(Set<RequestParticipants> participants) {
        this.participants = participants;
    }
}
