package com.mycompany.hrms.data.repository.jobs;

import com.mycompany.hrms.data.entity.job.JobShared;
import com.mycompany.hrms.data.entity.job.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSharedRepo extends JpaRepository<JobShared , Long> {
    List<JobShared> findJobSharedBySharedBy_UserId(long sharedByUserId);

    List<JobShared> findJobSharedByJob_JobId(long jobId);

    long job(Jobs job);
}
