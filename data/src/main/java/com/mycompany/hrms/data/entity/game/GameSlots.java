package com.mycompany.hrms.data.entity.game;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "slotId")
@Entity
public class GameSlots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long slotId;

    @Column(nullable = false)
    private ZonedDateTime startTime;

    @Column(nullable = false)
    private ZonedDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SlotStatus status = SlotStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameId")
    private GameConfig gameConfig;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "gameSlots")
    private Set<SlotRequest> slotRequests;

    public enum SlotStatus {
        OPEN,
        PROCESSING,
        BOOKED,
        NO_BOOKING,
        LOCKED
    }

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public Set<SlotRequest> getSlotRequests() {
        return slotRequests;
    }

    public void setSlotRequests(Set<SlotRequest> slotRequests) {
        this.slotRequests = slotRequests;
    }
}
