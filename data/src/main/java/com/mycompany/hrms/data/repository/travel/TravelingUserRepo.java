package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.TravelGallery;
import com.mycompany.hrms.data.entity.travel.TravelingUser;
import com.mycompany.hrms.data.entity.user.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    Optional<TravelingUser> getTravelingUsersByUser_UserIdAndTravelDetails_TravelId(long userUserId, long travelDetailsTravelId);

    @Query("SELECT u FROM TravelingUser u " +
            "LEFT JOIN FETCH u.travelDetails t " +
            "LEFT JOIN FETCH t.createdBy c " +
            "LEFT JOIN FETCH t.travelGallery g " +
            "WHERE u.user.userId = :id")
    List<TravelingUser> getTravelingUsersByUser_UserId(@Param("id") long id);

    @Query(value = "select u.* from TravelingUser u left join users v on u.userId = v.userId where v.assignedUnder = :managerId and u.travelId = :travelId", nativeQuery = true)
    List<TravelingUser> getTravelingUsersAssignUnder(@Param("managerId") long managerId, @Param("travelId") long travelId);

    boolean existsByUser_UserIdAndTravelDetails_TravelId(long userUserId, long travelDetailsTravelId);
}