package com.mycompany.hrms.api.controllers.game;

import com.mycompany.hrms.service.dtos.game.request.AddGameInterest;
import com.mycompany.hrms.service.game.IGameInterestService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-interest")
public class GameInterestController {

    private final IGameInterestService gameInterestService;

    @Autowired
    public GameInterestController(IGameInterestService gameInterestService){
        this.gameInterestService = gameInterestService;
    }

    @Operation(
            summary = "Add game as interested"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @PostMapping("")
    public ResponseEntity<Void> addGameInterest(@RequestBody AddGameInterest gameInterest){
        gameInterestService.addGameInterest(gameInterest.getUserId(), gameInterest.getGameId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Remove game as interested"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @DeleteMapping("")
    public ResponseEntity<Void> removeGameInterest(@RequestBody AddGameInterest gameInterest){
        gameInterestService.removeGameInterest(gameInterest.getUserId(), gameInterest.getGameId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
