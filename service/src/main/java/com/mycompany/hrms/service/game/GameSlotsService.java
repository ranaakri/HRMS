package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.repository.game.GameConfigRepo;
import com.mycompany.hrms.data.repository.game.GameSlotsRepo;
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
public class GameSlotsService implements IGameSlotsService{

    private final GameConfigRepo gameConfigRepo;
    private final GameSlotsRepo gameSlotsRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public GameSlotsService(GameConfigRepo gameConfigRepo, GameSlotsRepo gameSlotsRepo, ModelMapper modelMapper){
        this.gameConfigRepo = gameConfigRepo;
        this.gameSlotsRepo = gameSlotsRepo;
        this.modelMapper = modelMapper;
    }

    public List<GameSlotResponse> getTodaySlots(long gameId){
        if(!gameConfigRepo.existsById(gameId))
            throw new ResourceNotFoundException("Game does not exist");
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        LocalDate today = LocalDate.now(zoneId);
        return gameSlotsRepo.findAllByStartTimeBetweenAndGameConfig_GameId(today.atStartOfDay(zoneId), today.atTime(LocalTime.MAX).atZone(zoneId), gameId)
                .stream().map(val -> modelMapper.map(val, GameSlotResponse.class)).toList();
    }
}