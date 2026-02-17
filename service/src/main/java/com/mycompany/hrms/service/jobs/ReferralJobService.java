package com.mycompany.hrms.service.jobs;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.job.Jobs;
import com.mycompany.hrms.data.entity.job.Referrals;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.jobs.JobRepo;
import com.mycompany.hrms.data.repository.jobs.ReferralsRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.job.request.ReferralJobReq;
import com.mycompany.hrms.service.dtos.job.response.ReferralJobRes;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReferralJobService implements IReferralService {

    private final ReferralsRepo referralsRepo;
    private final ModelMapper modelMapper;
    private final JobRepo jobRepo;
    private final UsersRepo usersRepo;

    @Autowired
    public ReferralJobService(ReferralsRepo referralsRepo, ModelMapper modelMapper, JobRepo jobRepo, UsersRepo usersRepo){
        this.referralsRepo = referralsRepo;
        this.modelMapper = modelMapper;
        this.jobRepo = jobRepo;
        this.usersRepo = usersRepo;
    }

    public ReferralJobRes createReferral(ReferralJobReq request){
        Users users = usersRepo.findById(request.getReferredById())
                .orElseThrow(() -> new ResourceNotFoundException("Referred by user not found"));
        Jobs jobs = jobRepo.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        Referrals referrals = modelMapper.map(request, Referrals.class);
        referrals.setJob(jobs);
        referrals.setReferredBy(users);
        return modelMapper.map(referralsRepo.save(referrals), ReferralJobRes.class);
    }

    public List<ReferralJobRes> getListOfReferralsByJoId(long jobId){
        if(!jobRepo.existsById(jobId))
            throw new ResourceNotFoundException("Job not found");
        List<Referrals> referrals = referralsRepo.findAllByJob_JobId(jobId);
        return referrals.stream().map(val -> modelMapper.map(val, ReferralJobRes.class)).toList();
    }

    public List<ReferralJobRes> getListOfReferralsByUserId(long userId){
        if(!usersRepo.existsById(userId))
            throw new ResourceNotFoundException("User not found");
        List<Referrals> referrals = referralsRepo.findAllByReferredBy_UserId(userId);
        return referrals.stream().map(val -> modelMapper.map(val, ReferralJobRes.class)).toList();
    }

    public ReferralJobRes updateStatus(long referralId, Constants.JobStatus status){
        Referrals referrals = referralsRepo.findById(referralId)
                .orElseThrow(() -> new ResourceNotFoundException("Referral not found"));
        referrals.setStatus(status);
        return modelMapper.map(referrals, ReferralJobRes.class);
    }

    public void deleteReferral(long referralId){
        Referrals referrals = referralsRepo.findById(referralId)
                .orElseThrow(() -> new ResourceNotFoundException("Referral not found"));
        referralsRepo.delete(referrals);
    }
}
