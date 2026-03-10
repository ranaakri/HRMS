package com.mycompany.hrms.data.repository.game;

import com.mycompany.hrms.data.constant.SlotEventRes;
import com.mycompany.hrms.data.entity.game.GameSlots;
import com.mycompany.hrms.data.entity.game.SlotRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface SlotRequestRepo extends JpaRepository<SlotRequest, Long> {

    @Query(value = """
            SELECT * FROM GameSlots g
            WHERE g.startTime BETWEEN
                DATEADD(MINUTE, 14, SYSDATETIMEOFFSET()) AND
                DATEADD(MINUTE, 16, SYSDATETIMEOFFSET())""", nativeQuery = true)
    List<GameSlots> findSlotsStartingSoon();

    @Query("SELECT new com.mycompany.hrms.data.constant.SlotEventRes(" +
            "sl.requestId, gs.gameConfig.gameId, gs.startTime, gs.endTime, sl.status, gs.gameConfig.name) " +
            "FROM RequestParticipants rp " +
            "JOIN rp.request sl " +
            "JOIN sl.gameSlots gs " +
            "WHERE rp.user.userId = :userId")
    List<SlotEventRes> findAllSlotRequest(@Param("userId") long userId);


    @Query(
            """
                        SELECT r FROM SlotRequest r
                        LEFT JOIN FETCH r.participants p
                        LEFT JOIN FETCH p.user
                        WHERE r.gameSlots.slotId = :slotId
                    """
    )
    List<SlotRequest> findAllByGameSlots_SlotId(@Param("slotId") Long slotId);

    List<SlotRequest> findAllByGameSlots_SlotIdAndStatus(long slotId, SlotRequest.RequestStatus status);

    Optional<SlotRequest> findByGameSlots_SlotIdAndRequestBy_UserId(long slotId, long userId);

    boolean existsByGameSlots_SlotIdAndParticipants_User_UserIdIn(Long slotId, List<Long> userId);

    @Query(value = "select rs.userId AS participantId from GameSlots gs join SlotRequest sr on gs.slotId = sr.slotId join RequestParticipants rs on sr.requestId = rs.requestId where gs.slotId = :slotId and sr.requestId = :requestId", nativeQuery = true)
    List<Long> findAllParticipantsId(@Param("slotId") long slotId, @Param("requestId") long requestId);

    @Query(value = "select s.* from SlotRequest s join RequestParticipants rp on rp.requestId = s.requestId where rp.userId = :userId and s.slotId = :slotId", nativeQuery = true)
    Optional<SlotRequest> findByGameSlots_SlotIdAndUser_SlotId(@Param("slotId") long slotId, @Param("userId") long userId);

    boolean existsByParticipants_User_UserIdInAndStatusAndRequestTimeStampBefore(List<Long> userIds, SlotRequest.RequestStatus status, ZonedDateTime time);
}
