package com.mycompany.hrms.data.repository.jobs;

import com.mycompany.hrms.data.entity.job.Jobs;
import com.mycompany.hrms.data.entity.job.Referrals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferralsRepo extends JpaRepository<Referrals, Long> {
    List<Referrals> findAllByJob_JobId(long jobJobId);

    List<Referrals> findAllByReferredBy_UserId(long referredByUserId);
    long job(Jobs job);
}
