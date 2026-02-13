package com.mycompany.hrms.data.repository.jobs;

import com.mycompany.hrms.data.entity.job.JobShared;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSharedRepo extends JpaRepository<JobShared , Long> {
}
