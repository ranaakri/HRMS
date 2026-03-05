package com.mycompany.hrms.service.jobs;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.job.request.ReferralJobReq;
import com.mycompany.hrms.data.dtos.job.response.JobRes;
import com.mycompany.hrms.data.dtos.job.response.ReferralJobRes;

import java.util.List;

public interface IReferralService {
    List<ReferralJobRes> getListOfReferralsByJoId(long jobId);
    List<ReferralJobRes> getListOfReferralsByUserId(long userId);
    ReferralJobRes updateStatus(long referralId, Constants.JobStatus status);
    void deleteReferral(long referralId);
    ReferralJobRes createReferral(ReferralJobReq request);
    List<JobRes> getJobsForCvReviews(long userId);
    List<ReferralJobRes> getReferralsForCvReview(long jobId, long userId);
}
