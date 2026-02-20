package com.mycompany.hrms.data.repository.game;

import com.mycompany.hrms.data.entity.game.GameSlots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface GameSlotsRepo extends JpaRepository<GameSlots , Long> {
    List<GameSlots> findAllByStartTimeBetweenAndGameConfig_GameId(ZonedDateTime startTimeAfter, ZonedDateTime startTimeBefore, long gameConfigGameId);
}
