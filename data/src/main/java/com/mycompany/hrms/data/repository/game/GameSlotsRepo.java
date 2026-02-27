package com.mycompany.hrms.data.repository.game;

import com.mycompany.hrms.data.entity.game.GameSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface GameSlotsRepo extends JpaRepository<GameSlots , Long> {
    List<GameSlots> findAllByStartTimeBetweenAndGameConfig_GameId(ZonedDateTime startTimeAfter, ZonedDateTime startTimeBefore, long gameConfigGameId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
                   "FROM GameSlots " +
                   "WHERE startTime >= CAST(GETDATE() AS DATE) " +
                   "AND startTime < CAST(DATEADD(day, 1, GETDATE()) AS DATE)",
            nativeQuery = true)
    int existsSlotsForToday();

    @Query(value = "select top 6 * from GameSlots where startTime >= SYSDATETIMEOFFSET() AND gameId = :gameId", nativeQuery = true)
    List<GameSlots> getTop5LatestSlots(@Param("gameId") long gameId);
}
