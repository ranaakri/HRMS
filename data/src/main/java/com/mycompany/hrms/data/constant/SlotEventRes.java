package com.mycompany.hrms.data.constant;

import com.mycompany.hrms.data.entity.game.SlotRequest;

import java.time.ZonedDateTime;

public class SlotEventRes {
    private long id;
    private long resourceId;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private String title;
    private SlotRequest.RequestStatus status;

    public SlotEventRes(long gameId, long requestId, ZonedDateTime startTime, ZonedDateTime endTime, SlotRequest.RequestStatus status, String name) {
        this.id = gameId;
        this.resourceId = requestId;
        this.start = startTime;
        this.end = endTime;
        this.status = status;
        this.title = name + " Booking";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SlotRequest.RequestStatus getStatus() {
        return status;
    }

    public void setStatus(SlotRequest.RequestStatus status) {
        this.status = status;
    }
}
