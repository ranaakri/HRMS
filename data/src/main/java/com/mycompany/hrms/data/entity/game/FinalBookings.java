package com.mycompany.hrms.data.entity.game;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "requestId")
@Entity
public class FinalBookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "slotId")
    private GameSlots gameSlot;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmedRequestId")
    private SlotRequest confirmedRequest;

    private boolean isCompleted = false;
}
