package com.mycompany.hrms.service.dtos.game.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

public class CreateGameReq {

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    @Max(4)
    private int minPlayers = 1;

    @NotNull
    @Min(2)
    @Max(4)
    private int maxPlayers;

    @NotNull
    private int slotDuration;

    @NotNull
    private LocalTime openTime;

    @NotNull
    private LocalTime closeTime;

    @NotNull
    private boolean isActive = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getSlotDuration() {
        return slotDuration;
    }

    public void setSlotDuration(int slotDuration) {
        this.slotDuration = slotDuration;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
