package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.TravelingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TravelingUserRepo extends JpaRepository<TravelingUser, Long> {
    @Procedure(name = "dbo.sp_I_travelingUser")
    void sp_I_travelingUser(
            @Param("userId") long userId,
            @Param("travelId") long travelId,
            @Param("travelBalance") float travelBalance
    );

    @Procedure(name = "dbo.sp_U_travelingUser")
    void sp_U_travelingUser(
            @Param("travelingUserId") long travelingUserId,
            @Param("travelBalance") float travelBalance
    );

    List<TravelingUser> getTravelingUsersByTravelDetails_TravelId(long travelDetailsTravelId);
}