package com.mycompany.hrms.data.repository.jobs;

import com.mycompany.hrms.data.entity.job.Referrals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferralsRepo extends JpaRepository<Referrals, Long> {
}
