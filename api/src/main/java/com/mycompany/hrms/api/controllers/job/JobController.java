package com.mycompany.hrms.api.controllers.job;

import com.mycompany.hrms.service.dtos.job.request.CreateJobReq;
import com.mycompany.hrms.service.dtos.job.request.UpdateJobReq;
import com.mycompany.hrms.service.dtos.job.response.JobRes;
import com.mycompany.hrms.service.jobs.IJobService;
import com.mycompany.hrms.service.jobs.JobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {

    private final IJobService jobService;

    @Autowired
    public JobController(IJobService jobService){
        this.jobService = jobService;
    }

    @Operation(
            summary = "Get job by id"
    )
    @GetMapping("/{jobId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Eployee', 'Manager')")
    public ResponseEntity<JobRes> getJobById(@PathVariable long jobId){
        return ResponseEntity.ok(jobService.findJobById(jobId));
    }

    @Operation(
            summary = "List open jobs"
    )
    @PreAuthorize("hasAnyAuthority('Employee', 'Manager', 'HR')")
    @GetMapping("/open")
    public ResponseEntity<List<JobRes>> getOpenJobs() {
        return ResponseEntity.ok(jobService.listOpenJobs());
    }

    @Operation(
            summary = "Get All jobs"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @GetMapping("")
    public ResponseEntity<List<JobRes>> getJobs(){
        return ResponseEntity.ok(jobService.listAllJobs());
    }

    @Operation(
            summary = "Create new job"
    )
    @PostMapping(path = "")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<JobRes> createJob(@RequestBody CreateJobReq req){
        return new ResponseEntity<>(jobService.createJob(req), HttpStatus.CREATED);
    }

    @Operation(
            summary = "upload Job description doc for job"
    )
    @PreAuthorize("hasAuthority('HR')")
    @PostMapping(path = "/jd/{jobId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadJd(@RequestParam MultipartFile jdFile, @PathVariable long jobId){
        return ResponseEntity.ok(jobService.uploadJd(jobId, jdFile));
    }

    @Operation(
            summary = "Update job information"
    )
    @PutMapping("/{jobId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<JobRes> updateJob(@RequestBody UpdateJobReq req, @PathVariable long jobId){
        return ResponseEntity.ok(jobService.updateJob(jobId, req));
    }

    @Operation(
            summary = "Delete job by id"
    )
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<String> deleteJob(@PathVariable long jobId){
        jobService.deleteJob(jobId);
        return ResponseEntity.ok("Job deleted");
    }
}
