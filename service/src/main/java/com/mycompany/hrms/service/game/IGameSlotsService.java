package com.mycompany.hrms.service.game;

import com.mycompany.hrms.service.dtos.game.response.GameEventResponse;
import com.mycompany.hrms.service.dtos.game.response.GameSlotResponse;

import java.util.List;

public interface IGameSlotsService {
    List<GameSlotResponse> getTodaySlots(long gameId);

    GameSlotResponse getSlotInfo(long slotId);

    GameEventResponse getEventData(long userId);
}
