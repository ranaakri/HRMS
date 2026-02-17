package com.mycompany.hrms.service.jobs;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.service.dtos.job.request.JobShareReq;
import com.mycompany.hrms.service.dtos.job.response.JobSharedRes;

import java.util.List;

public interface IJobSharedService {
    JobSharedRes createShareJob(JobShareReq jobShareReq);
    List<JobSharedRes> getSharedByUserId(long userId);
    List<JobSharedRes> getSharedByJobId(long jobId);
    JobSharedRes updateJobStatus(long jobSharedId, Constants.JobStatus status);
    void deleteShared(long jobSharedId);
    JobSharedRes getShareBySharedId(long sharedId);
}
