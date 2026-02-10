package com.mycompany.hrms.data.repository.travel;

import com.mycompany.hrms.data.entity.travel.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPlanRepo extends JpaRepository<TravelPlan, Long> {
}
