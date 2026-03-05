package com.mycompany.hrms.data.dtos.game.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BookGameSlot {

    @NotNull
    private List<Long> userIds;

    @NotNull
    private long requestedBy;
    @NotNull
    private long slotId;

    public long getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(long requestedBy) {
        this.requestedBy = requestedBy;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }
}
