package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.TravelDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelDetailsRepo extends JpaRepository<TravelDetails, Long> {
    List<TravelDetails> findAllByCreatedBy_UserId(long userId);
}
