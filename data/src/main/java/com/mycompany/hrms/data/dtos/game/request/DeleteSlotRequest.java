package com.mycompany.hrms.data.dtos.game.request;

import jakarta.validation.constraints.NotNull;

public class DeleteSlotRequest {

    @NotNull
    private long slotId;

    @NotNull
    private long requestedBy;

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public long getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(long requestedBy) {
        this.requestedBy = requestedBy;
    }
}
