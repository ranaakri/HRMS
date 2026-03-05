package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.dtos.game.request.CreateGameReq;
import com.mycompany.hrms.data.dtos.game.response.GameResponse;

import java.util.List;

public interface IGameConfigService {
    List<GameResponse> getAllGames(long userId);

    GameResponse createGame(CreateGameReq createGameReq);

    void makeGameInactive(long gameId);

    GameResponse updateGame(long gameId, CreateGameReq updateGame);

    List<GameResponse> getAllActiveGames(long userId);

    GameResponse getGame(long gameId);

    void deleteGame(long gameId);
}
