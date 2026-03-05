package com.mycompany.hrms.data.dtos.game.response;

public class UserPriorityRes {

    public UserPriorityRes(long requestId, int priority, RequestedByUser requestedBy){

        this.requestId = requestId;
        this.priority = priority;
        this.requestedBy = requestedBy;
    }

    private long requestId;
    private int priority;
    private RequestedByUser requestedBy;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public RequestedByUser getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(RequestedByUser requestedBy) {
        this.requestedBy = requestedBy;
    }
}
