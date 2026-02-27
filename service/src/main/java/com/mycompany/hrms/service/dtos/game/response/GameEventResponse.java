package com.mycompany.hrms.service.dtos.game.response;

import com.mycompany.hrms.data.constant.SlotEventRes;

import java.util.List;

public class GameEventResponse {
    private List<GameResources> resources;

    private List<SlotEventRes> events;

    public List<GameResources> getResources() {
        return resources;
    }

    public void setResources(List<GameResources> resources) {
        this.resources = resources;
    }

    public List<SlotEventRes> getEvents() {
        return events;
    }

    public void setEvents(List<SlotEventRes> events) {
        this.events = events;
    }
}
