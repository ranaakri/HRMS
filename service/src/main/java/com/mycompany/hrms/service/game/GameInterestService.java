package com.mycompany.hrms.service.game;

import com.mycompany.hrms.data.entity.game.GameConfig;
import com.mycompany.hrms.data.entity.game.UserGameStats;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.game.GameConfigRepo;
import com.mycompany.hrms.data.repository.game.UserGameStatesRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameInterestService implements IGameInterestService {

    private final UserGameStatesRepo userGameStatesRepo;
    private final UsersRepo usersRepo;
    private final GameConfigRepo gameConfigRepo;

    @Autowired
    public GameInterestService(UserGameStatesRepo userGameStatesRepo, UsersRepo usersRepo, GameConfigRepo gameConfigRepo) {
        this.userGameStatesRepo = userGameStatesRepo;
        this.usersRepo = usersRepo;
        this.gameConfigRepo = gameConfigRepo;
    }

    @Transactional
    public void addGameInterest(long userId, long gameId) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        GameConfig game = gameConfigRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        UserGameStats gameStats = userGameStatesRepo.findByUser_UserIdAndGameConfig_GameId(userId, gameId)
                .orElse(null);
        if (gameStats == null) {
            UserGameStats stats = new UserGameStats();
            stats.setUser(user);
            stats.setGameConfig(game);
            userGameStatesRepo.save(stats);
        } else {
            gameStats.setInterested(true);
        }
    }

    @Transactional
    public void removeGameInterest(long userId, long gameId) {
        UserGameStats stats = userGameStatesRepo.findByUser_UserIdAndGameConfig_GameId(userId, gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Not a game interest"));
        stats.setInterested(false);
    }
}
