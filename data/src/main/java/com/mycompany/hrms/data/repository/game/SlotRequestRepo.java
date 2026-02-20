package com.mycompany.hrms.data.repository.game;

import com.mycompany.hrms.data.entity.game.GameSlots;
import com.mycompany.hrms.data.entity.game.SlotRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SlotRequestRepo extends JpaRepository<SlotRequest, Long> {

    @Query(value = """
            SELECT * FROM GameSlots g
            WHERE g.startTime BETWEEN
                DATEADD(MINUTE, 10, SYSDATETIMEOFFSET()) AND
                DATEADD(MINUTE, 31, SYSDATETIMEOFFSET())""", nativeQuery = true)
    List<GameSlots> findSlotsStartingSoon();

    List<SlotRequest> findAllByGameSlots_SlotId(Long slotId);

    boolean existsByGameSlots_SlotIdAndParticipants_User_UserIdIn(Long slotId, List<Long> userId);

    @Query(value = "select rs.userId from GameSlots gs join SlotRequest sr on gs.slotId = sr.slotId join RequestParticipants rs on sr.requestId = rs.requestId where gs.slotId = :slotId and sr.requestId = :requestId", nativeQuery = true)
    List<Long> findAllParticipantsId(@Param("slotId") long slotId,@Param("requestId") long requestId);
}
