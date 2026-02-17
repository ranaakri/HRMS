package com.mycompany.hrms.service.jobs;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.job.JobShared;
import com.mycompany.hrms.data.entity.job.Jobs;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.jobs.JobRepo;
import com.mycompany.hrms.data.repository.jobs.JobSharedRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.job.request.JobShareReq;
import com.mycompany.hrms.service.dtos.job.response.JobSharedRes;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSharedService implements IJobSharedService{

    private final JobSharedRepo jobSharedRepo;
    private final ModelMapper modelMapper;
    private final UsersRepo usersRepo;
    private final JobRepo jobRepo;

    @Autowired
    public JobSharedService(JobSharedRepo jobSharedRepo, ModelMapper modelMapper, UsersRepo usersRepo, JobRepo jobRepo){
        this.jobSharedRepo = jobSharedRepo;
        this.modelMapper = modelMapper;
        this.usersRepo = usersRepo;
        this.jobRepo = jobRepo;
    }

    public JobSharedRes createShareJob(JobShareReq jobShareReq){
        Users user = usersRepo.findById(jobShareReq.getSharedBy())
                .orElseThrow(() -> new ResourceNotFoundException("Shared by users's data not found"));

        Jobs job = jobRepo.findById(jobShareReq.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        JobShared jobShared = modelMapper.map(jobShareReq, JobShared.class);
        jobShared.setSharedBy(user);
        jobShared.setJob(job);

        return modelMapper.map(jobSharedRepo.save(jobShared), JobSharedRes.class);
    }

    public JobSharedRes getShareBySharedId(long sharedId){
        JobShared jobShared = jobSharedRepo.findById(sharedId)
                .orElseThrow(() -> new ResourceNotFoundException("Shared jobs reference not found"));
        return modelMapper.map(jobShared, JobSharedRes.class);
    }

    public List<JobSharedRes> getSharedByUserId(long userId){
        List<JobShared> shared = jobSharedRepo.findJobSharedBySharedBy_UserId(userId);
        return shared.stream().map(val -> modelMapper.map(val, JobSharedRes.class)).toList();
    }

    public List<JobSharedRes> getSharedByJobId(long jobId){
        List<JobShared> shared = jobSharedRepo.findJobSharedByJob_JobId(jobId);
        return shared.stream().map(val -> modelMapper.map(val, JobSharedRes.class)).toList();
    }

    @Transactional
    public JobSharedRes updateJobStatus(long jobSharedId, Constants.JobStatus status){
        JobShared jobShared = jobSharedRepo.findById(jobSharedId)
                .orElseThrow(() -> new ResourceNotFoundException("Shared job reference not found"));
        jobShared.setStatus(status);
        return modelMapper.map(jobShared, JobSharedRes.class);
    }

    public void deleteShared(long jobSharedId){
        JobShared jobShared = jobSharedRepo.findById(jobSharedId)
                .orElseThrow(() -> new ResourceNotFoundException("Shared job reference not found"));
        jobSharedRepo.delete(jobShared);
    }
}
