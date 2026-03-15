package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.GameConfig;
import com.mycompany.hrms.data.entity.game.UserGameStats;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.game.GameConfigRepo;
import com.mycompany.hrms.data.repository.game.UserGameStatesRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.game.request.CreateGameReq;
import com.mycompany.hrms.data.dtos.game.response.GameResponse;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class GameConfigService implements IGameConfigService {

    private final ModelMapper modelMapper;
    private final GameConfigRepo gameConfigRepo;
    private final UsersRepo usersRepo;
    private final UserGameStatesRepo userGameStatesRepo;

    private static final String GAME_NOT_FOUND = "Game not found";

    public GameConfigService(ModelMapper modelMapper, GameConfigRepo gameConfigRepo, UsersRepo usersRepo, UserGameStatesRepo userGameStatesRepo) {
        this.modelMapper = modelMapper;
        this.gameConfigRepo = gameConfigRepo;
        this.usersRepo = usersRepo;
        this.userGameStatesRepo = userGameStatesRepo;
    }

    public GameResponse getGame(long gameId) {
        GameConfig game = gameConfigRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException(GAME_NOT_FOUND));
        return modelMapper.map(game, GameResponse.class);
    }

    public List<GameResponse> getAllActiveGames(long userId) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() ->  new ResourceNotFoundException("User not found"));

        return gameConfigRepo.findByIsActiveTrue().stream().map(val -> {
            GameResponse res = modelMapper.map(val, GameResponse.class);
            UserGameStats gameStats = userGameStatesRepo.findByUser_UserIdAndGameConfig_GameId(userId, res.getGameId())
                    .orElse(null);
            if (gameStats == null)
                res.setInterested(false);
            else
                res.setInterested(gameStats.isInterested());
            if (val.getLikedBy().contains(user))
                res.setFavourite(true);
            return res;
        }).toList();
    }



    public List<GameResponse> getAllGames(long userId) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() ->  new ResourceNotFoundException("User not found"));

        return gameConfigRepo.findAll().stream().map(val -> {
            GameResponse res = modelMapper.map(val, GameResponse.class);
            UserGameStats gameStats = userGameStatesRepo.findByUser_UserIdAndGameConfig_GameId(userId, res.getGameId())
                    .orElse(null);
            if (gameStats == null)
                res.setInterested(false);
            else
                res.setInterested(gameStats.isInterested());
            if (val.getLikedBy().contains(user))
                res.setFavourite(true);
            return res;
        }).toList();
    }

    public GameResponse createGame(CreateGameReq createGameReq) {
        if (createGameReq.getMinPlayers() > createGameReq.getMaxPlayers()) {
            throw new BadRequestException("Minimum players can not be grater then maximum players");
        }
        if (createGameReq.getOpenTime().compareTo(createGameReq.getCloseTime()) >= 10) {
            throw new BadRequestException("Minimum slot should be at least of 10 minutes");
        }
        if(createGameReq.getOpenTime().isAfter(createGameReq.getCloseTime()))
            throw new BadRequestException("Invalid opening and closing time");
        if(ChronoUnit.MINUTES.between(createGameReq.getOpenTime(), createGameReq.getCloseTime()) < createGameReq.getSlotDuration())
            throw new BadRequestException("Opening and closing time should be more then slot duration");
        GameConfig game = modelMapper.map(createGameReq, GameConfig.class);
        return modelMapper.map(gameConfigRepo.save(game), GameResponse.class);
    }

    @Transactional
    public GameResponse updateGame(long gameId, CreateGameReq updateGame) {
        GameConfig game = gameConfigRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException(GAME_NOT_FOUND));
        if (updateGame.getMinPlayers() > updateGame.getMaxPlayers()) {
            throw new BadRequestException("Minimum players can not be grater then maximum players");
        }
        if (updateGame.getOpenTime().compareTo(updateGame.getCloseTime()) >= 10) {
            throw new BadRequestException("Minimum slot should be at least of 10 minutes");
        }
        if(updateGame.getOpenTime().isAfter(updateGame.getCloseTime()))
            throw new BadRequestException("Invalid opening and closing time");
        if(ChronoUnit.MINUTES.between(updateGame.getOpenTime(), updateGame.getCloseTime()) < updateGame.getSlotDuration())
            throw new BadRequestException("Opening and closing time should be more then slot duration");
        modelMapper.map(updateGame, game);
        return modelMapper.map(game, GameResponse.class);
    }

    @Transactional
    public void makeGameInactive(long gameId) {
        GameConfig gameConfig = gameConfigRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException(GAME_NOT_FOUND));
        gameConfig.setActive(false);
    }

    public void deleteGame(long gameId){
        GameConfig gameConfig = gameConfigRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        gameConfigRepo.delete(gameConfig);
    }
}
