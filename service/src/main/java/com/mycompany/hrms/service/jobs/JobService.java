package com.mycompany.hrms.service.jobs;

import com.mycompany.hrms.data.repository.jobs.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final JobRepo jobRepo;

    @Autowired
    public JobService(JobRepo jobRepo){
        this.jobRepo = jobRepo;
    }


}
