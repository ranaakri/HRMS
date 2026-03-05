package com.mycompany.hrms.data.dtos.game.request;

import jakarta.validation.constraints.NotNull;

public class AddGameInterest {

    @NotNull
    private long userId;

    @NotNull
    private long gameId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
}