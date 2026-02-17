package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import com.mycompany.hrms.data.entity.travel.TravelDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TravelDocumentsRepo extends JpaRepository<TravelDocuments, Long> {
    @Query("SELECT d FROM TravelDocuments d " +
            "WHERE d.travelingUser.user.userId = :userId " +
            "AND d.travelingUser.travelDetails.travelId = :travelId")
    List<TravelDocuments> getTravelDocsForUser(@Param("userId") long userId,
                                               @Param("travelId") long travelId);

    List<TravelDocuments> getTravelDocumentsByTravelingUser_TravelingUserId(long travelingUserId);
}
