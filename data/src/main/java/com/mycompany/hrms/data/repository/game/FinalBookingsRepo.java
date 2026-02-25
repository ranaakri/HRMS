package com.mycompany.hrms.data.repository.game;

import com.mycompany.hrms.data.entity.game.FinalBookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface FinalBookingsRepo extends JpaRepository<FinalBookings, Long> {
    FinalBookings findByGameSlot_SlotId(long gameSlotSlotId);

    @Query("""
        select count(fb) > 0 from FinalBookings fb join fb.confirmedRequest sr join sr.participants rp where rp.user.userId = :userId
                and fb.isCompleted = false
                and fb.gameSlot.startTime > :now
        """)
    boolean existsActiveBooking(@Param("userId") long userId, @Param("now")ZonedDateTime now);

    boolean existsByConfirmedRequest_RequestId(long requestId);

    @Query(value = "select f.* from FinalBookings f join SlotRequest s on s.requestId = f.confirmedRequestId where s.requestBy = :userId and s.slotId = :slotId", nativeQuery = true)
    Optional<FinalBookings> findByUser_UserIdAndGameSlots_SlotId(@Param("userId") long userId, @Param("slotId") long slotId);

    @Query("Select fb from FinalBookings fb where fb.isCompleted=false and fb.gameSlot.endTime <= :now")
    List<FinalBookings> findPastIncompleteBookings(@Param("now") ZonedDateTime now);
}