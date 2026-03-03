package com.mycompany.hrms.data.repository.jobs;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.job.Jobs;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepo extends JpaRepository<Jobs, Long> {
    List<Jobs> findAllByStatus(Constants.JobDataStatus status);
    @Query("SELECT j FROM Jobs j LEFT JOIN FETCH j.cvReviewers WHERE j.id = :id")
    Optional<Jobs> findByIdWithReviewers(@Param("id") Long id);

    List<Jobs> findAllByCvReviewers_UserIdAndStatus(long userId, Constants.JobDataStatus status);
}
