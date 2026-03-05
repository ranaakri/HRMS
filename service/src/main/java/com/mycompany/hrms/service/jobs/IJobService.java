package com.mycompany.hrms.service.jobs;

import com.mycompany.hrms.data.dtos.job.request.CreateJobReq;
import com.mycompany.hrms.data.dtos.job.request.UpdateJobReq;
import com.mycompany.hrms.data.dtos.job.response.JobRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IJobService {
    JobRes createJob(CreateJobReq jobReq);
    String uploadJd(long jobId, MultipartFile file);
    JobRes findJobById(long jobId);
    List<JobRes> listAllJobs();
    JobRes updateJob(long jobId, UpdateJobReq jobReq);
    void deleteJob(long jobId);
    List<JobRes> listOpenJobs();
    List<JobRes> listLatestJobOpenings();
}
