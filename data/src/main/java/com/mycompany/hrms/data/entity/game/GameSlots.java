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
        LOCKED
    }
}
