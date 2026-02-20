package com.mycompany.hrms.service.dtos.game.request;

import java.util.List;

public class BookGameSlot {

    private List<Long> userIds;
    private long requestedBy;
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
