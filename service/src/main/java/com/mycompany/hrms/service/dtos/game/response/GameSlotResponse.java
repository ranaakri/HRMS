package com.mycompany.hrms.service.dtos.game.response;

import com.mycompany.hrms.data.entity.game.GameSlots;

import java.time.ZonedDateTime;

public class GameSlotResponse {
    private long slotId;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private GameSlots.SlotStatus status;

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

    public GameSlots.SlotStatus getStatus() {
        return status;
    }

    public void setStatus(GameSlots.SlotStatus status) {
        this.status = status;
    }
}
