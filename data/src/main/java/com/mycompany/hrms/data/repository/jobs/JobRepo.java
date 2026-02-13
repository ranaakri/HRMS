package com.mycompany.hrms.data.repository.jobs;

import com.mycompany.hrms.data.entity.job.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepo extends JpaRepository<Jobs, Long> {
}
