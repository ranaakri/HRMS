package com.mycompany.hrms.api.controllers.game;

import com.mycompany.hrms.data.dtos.game.request.CreateGameReq;
import com.mycompany.hrms.data.dtos.game.response.GameResponse;
import com.mycompany.hrms.service.game.IGameConfigService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameConfigController {

    private final IGameConfigService gameConfigService;

    @Autowired
    public GameConfigController(IGameConfigService gameConfigService){
        this.gameConfigService = gameConfigService;
    }

    @Operation(
            summary = "List all active games"
    )
    @GetMapping("/list/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<List<GameResponse>> getAllActive(@PathVariable long userId){
        return ResponseEntity.ok(gameConfigService.getAllActiveGames(userId));
    }

    @Operation(
            summary = "Get game info"
    )
    @GetMapping("/{gameId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<GameResponse> getGameInfo(@PathVariable long gameId){
        return ResponseEntity.ok(gameConfigService.getGame(gameId));
    }

    @Operation(
            summary = "List all games"
    )
    @GetMapping("/list-all/{userId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<List<GameResponse>> getAllGames(@PathVariable long userId){
        return ResponseEntity.ok(gameConfigService.getAllGames(userId));
    }

    @Operation(
            summary = "Make game inactive"
    )
    @PatchMapping("/{gameId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<Void> makeGameInactive(@PathVariable long gameId){
        gameConfigService.makeGameInactive(gameId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Create new game"
    )
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<GameResponse> createNewGame(@RequestBody CreateGameReq gameReq){
        return new ResponseEntity<>(gameConfigService.createGame(gameReq), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update game"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @PutMapping("/{gameId}")
    public ResponseEntity<GameResponse> updateGame(@PathVariable long gameId, @RequestBody CreateGameReq gameReq){
        return ResponseEntity.ok(gameConfigService.updateGame(gameId, gameReq));
    }

    @Operation(
            summary = "Delete game"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable long gameId){
        gameConfigService.deleteGame(gameId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
