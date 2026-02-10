package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.TravelDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelDocumentsRepo extends JpaRepository<TravelDocuments, Long> {
}
