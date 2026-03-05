package com.mycompany.hrms.service.jobs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.entity.job.Jobs;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.jobs.JobRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.job.request.CreateJobReq;
import com.mycompany.hrms.data.dtos.job.request.UpdateJobReq;
import com.mycompany.hrms.data.dtos.job.response.JobRes;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.InternalServerException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class JobService implements IJobService {

    private static final String JOB_NOT_FOUND = "Job not found";

    private final JobRepo jobRepo;
    private final ModelMapper modelMapper;
    private final UsersRepo usersRepo;
    private final Cloudinary cloudinary;

    @Autowired
    public JobService(JobRepo jobRepo, ModelMapper modelMapper, UsersRepo usersRepo, Cloudinary cloudinary){
        this.jobRepo = jobRepo;
        this.modelMapper = modelMapper;
        this.usersRepo = usersRepo;
        this.cloudinary = cloudinary;
    }

    @Transactional
    public String uploadJd(long jobId, MultipartFile file){
        Jobs job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));
        try{
            Map uploadedDoc = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "job/documents/")
            );

            String jdUrl = uploadedDoc.get("secure_url").toString();
            String publicId = uploadedDoc.get("public_id").toString();

            job.setJdFilePath(jdUrl);
            job.setPublicId(publicId);

            return jdUrl;
        }
        catch (Exception e) {
            throw new InternalServerException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public JobRes createJob(CreateJobReq jobReq) {
        Users hr = usersRepo.findById(jobReq.getHrId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!hr.getRole().getName().equals("HR"))
            throw new BadRequestException("User does not have role of HR");
        Jobs job = modelMapper.map(jobReq, Jobs.class);

        if (job.getCvReviewers() == null) {
            job.setCvReviewers(new HashSet<>());
        }

        job.setHrContact(hr);
        List<Users> cvReviewers = usersRepo.findAllById(jobReq.getCvReviewersList());
        job.getCvReviewers().addAll(cvReviewers);

        return modelMapper.map(jobRepo.save(job), JobRes.class);
    }

    public List<JobRes> listOpenJobs(){
        List<Jobs> openJobs = jobRepo.findAllByStatus(Constants.JobDataStatus.OPEN);

        return openJobs.stream().map(val-> modelMapper.map(val, JobRes.class)).toList();
    }

    public List<JobRes> listLatestJobOpenings(){
        return jobRepo.findAllByStatus(Constants.JobDataStatus.OPEN)
                .stream()
                .filter(val -> val.getLastApplicationDate().isAfter(ZonedDateTime.now()))
                .map(val -> modelMapper.map(val, JobRes.class))
                .toList();
    }

    public JobRes findJobById(long jobId){
        Jobs job = jobRepo.findByIdWithReviewers(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));
        return modelMapper.map(job, JobRes.class);
    }

    public List<JobRes> listAllJobs(){
        return jobRepo.findAll().stream().map(val -> modelMapper.map(val, JobRes.class)).toList();
    }

    @Transactional
    public JobRes updateJob(long jobId, UpdateJobReq jobReq){
        Jobs job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));
        modelMapper.map(jobReq, job);
        job.getCvReviewers().clear();
        List<Users> users = usersRepo.findAllById(jobReq.getCvReviewersList());
        job.getCvReviewers().addAll(users);
        return modelMapper.map(job,JobRes.class);
    }

    public void deleteJob(long jobId) {
        Jobs job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(JOB_NOT_FOUND));
        jobRepo.delete(job);
    }

}
