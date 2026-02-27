package com.mycompany.hrms.api.controllers.game;

import com.mycompany.hrms.service.dtos.game.response.GameEventResponse;
import com.mycompany.hrms.service.dtos.game.response.GameSlotResponse;
import com.mycompany.hrms.service.game.IGameSlotsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/game/slots")
public class GameSlotsController {

    private final IGameSlotsService gameSlotsService;

    @Autowired
    public GameSlotsController(IGameSlotsService gameSlotsService){
        this.gameSlotsService = gameSlotsService;
    }

    @Operation(
            summary = "Fetch today's slots"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @GetMapping("/{gameId}")
    public ResponseEntity<List<GameSlotResponse>> getTodaySlots(@PathVariable long gameId){
        return ResponseEntity.ok(gameSlotsService.getTodaySlots(gameId));
    }

    @Operation(
            summary = "Get all event data"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @GetMapping("/event-data/{userId}")
    public ResponseEntity<GameEventResponse> getGameEventData(@PathVariable long userId){
        return ResponseEntity.ok(gameSlotsService.getEventData(userId));
    }

    @Operation(
            summary = "Fetch game slot information"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @GetMapping("/info/{slotId}")
    public ResponseEntity<GameSlotResponse> getGameSlotInfo(@PathVariable long slotId){
        return ResponseEntity.ok(gameSlotsService.getSlotInfo(slotId));
    }
}
