package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.TravelDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelDocumentsRepo extends JpaRepository<TravelDocuments, Long> {
    List<TravelDocuments> getTravelDocumentsByTravelingUser_TravelingUserId(long travelingUserId);
}
