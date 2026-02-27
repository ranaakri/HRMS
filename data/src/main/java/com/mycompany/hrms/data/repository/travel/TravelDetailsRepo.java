package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelingUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TravelDetailsRepo extends JpaRepository<TravelDetails, Long> {
    List<TravelDetails> findAllByCreatedBy_UserId(long userId);

    @Query("SELECT t FROM TravelDetails t " +
            "JOIN t.travelingUsers u " +
            "LEFT JOIN FETCH t.createdBy c " +
            "LEFT JOIN FETCH t.travelGallery g " +
            "WHERE u.user.userId = :userId " +
            "AND t.startDate > CURRENT_TIMESTAMP " +
            "ORDER BY t.startDate ASC")
    List<TravelDetails> findFirstNearestTravel(@Param("userId") Long userId, Pageable pageable);

}
