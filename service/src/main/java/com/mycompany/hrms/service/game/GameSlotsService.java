package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.GameSlots;
import com.mycompany.hrms.data.entity.game.SlotRequest;
import com.mycompany.hrms.data.repository.game.GameConfigRepo;
import com.mycompany.hrms.data.repository.game.GameSlotsRepo;
import com.mycompany.hrms.data.repository.game.SlotRequestRepo;
import com.mycompany.hrms.service.dtos.game.response.GameEventResponse;
import com.mycompany.hrms.service.dtos.game.response.GameResources;
import com.mycompany.hrms.service.dtos.game.response.GameSlotResponse;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class GameSlotsService implements IGameSlotsService {

    private final GameConfigRepo gameConfigRepo;
    private final GameSlotsRepo gameSlotsRepo;
    private final ModelMapper modelMapper;
    private final SlotRequestRepo slotRequestRepo;

    @Autowired
    public GameSlotsService(GameConfigRepo gameConfigRepo,
                            GameSlotsRepo gameSlotsRepo,
                            SlotRequestRepo slotRequestRepo,
                            ModelMapper modelMapper) {
        this.gameConfigRepo = gameConfigRepo;
        this.gameSlotsRepo = gameSlotsRepo;
        this.modelMapper = modelMapper;
        this.slotRequestRepo = slotRequestRepo;
    }

    public GameSlotResponse getSlotInfo(long slotId) {
        GameSlots gameSlot = gameSlotsRepo.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid slot id"));
        return modelMapper.map(gameSlot, GameSlotResponse.class);
    }

    public GameEventResponse getEventData(long userId){
        GameEventResponse response = new GameEventResponse();
        response.setResources(gameConfigRepo.findAll().stream().map(val -> {
            GameResources gameResources = new GameResources();
            gameResources.setId(val.getGameId());
            gameResources.setTitle(val.getName());
            return gameResources;
        }).toList());
        response.setEvents(slotRequestRepo.findAllSlotRequest(userId));
        return response;
    }

    public List<GameSlotResponse> getTodaySlots(long gameId) {
        if (!gameConfigRepo.existsById(gameId))
            throw new ResourceNotFoundException("Game does not exist");
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        LocalDate today = LocalDate.now(zoneId);
        return gameSlotsRepo.findAllByStartTimeBetweenAndGameConfig_GameId(today.atStartOfDay(zoneId), today.atTime(LocalTime.MAX).atZone(zoneId), gameId)
                .stream().map(val -> modelMapper.map(val, GameSlotResponse.class)).toList();
    }
}