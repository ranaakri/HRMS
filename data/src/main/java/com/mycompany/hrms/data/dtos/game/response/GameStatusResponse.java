package com.mycompany.hrms.data.dtos.game.response;

import com.mycompany.hrms.data.entity.game.SlotRequest;
import com.mycompany.hrms.data.dtos.travel.response.CreatedByUser;

public class GameStatusResponse {
    private CreatedByUser requestBy;
    private SlotRequest.RequestStatus status;

    public CreatedByUser getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(CreatedByUser requestBy) {
        this.requestBy = requestBy;
    }

    public SlotRequest.RequestStatus getStatus() {
        return status;
    }

    public void setStatus(SlotRequest.RequestStatus status) {
        this.status = status;
    }
}
