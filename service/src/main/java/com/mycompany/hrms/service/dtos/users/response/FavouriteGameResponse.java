package com.mycompany.hrms.service.dtos.users.response;

import com.mycompany.hrms.service.dtos.game.response.GameSlotResponse;

import java.util.List;

public class FavouriteGameResponse {
    private String name;
    private long gameId;
    private List<GameSlotResponse> upComingSlots;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameSlotResponse> getUpComingSlots() {
        return upComingSlots;
    }

    public void setUpComingSlots(List<GameSlotResponse> upComingSlots) {
        this.upComingSlots = upComingSlots;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
}
