package com.mycompany.hrms.data.repository.game;

import com.mycompany.hrms.data.entity.game.UserGameStats;
import com.mycompany.hrms.data.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGameStatesRepo extends JpaRepository<UserGameStats, Long> {
    boolean existsByUser_UserIdAndGameConfig_GameId(long userUserId, long gameConfigGameId);

    Optional<UserGameStats> findByUser_UserIdAndGameConfig_GameId(long userUserId, long gameConfigGameId);
    List<UserGameStats> findByGameConfig_GameIdAndIsInterestedTrue(long gameId);
    List<UserGameStats> findByUser_UserIdInAndGameConfig_GameId(List<Long> userIds, long gameId);
    boolean existsByUser_UserIdInAndGameConfig_GameId(List<Long> userIds, long gameId);
}