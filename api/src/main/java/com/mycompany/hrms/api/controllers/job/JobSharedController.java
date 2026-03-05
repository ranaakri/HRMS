package com.mycompany.hrms.api.controllers.job;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.job.request.JobShareReq;
import com.mycompany.hrms.data.dtos.job.response.JobSharedRes;
import com.mycompany.hrms.service.jobs.IJobSharedService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-shared")
public class JobSharedController {

    private final IJobSharedService jobSharedService;

    @Autowired
    public JobSharedController(IJobSharedService jobSharedService){
        this.jobSharedService = jobSharedService;
    }

    @Operation(
            summary = "Get shared job by userId"
    )
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    public ResponseEntity<List<JobSharedRes>> getSharedJobByUserId(@PathVariable long userId){
        return ResponseEntity.ok(jobSharedService.getSharedByUserId(userId));
    }

    @Operation(
            summary = "Get shared job by job id"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobSharedRes>> getSharedJobByJobId(@PathVariable long jobId){
        return ResponseEntity.ok(jobSharedService.getSharedByJobId(jobId));
    }

    @Operation(
            summary = "Get shared job by shared id"
    )
    @GetMapping("/{sharedId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<JobSharedRes> getSharedJobBySharedId(@PathVariable long sharedId){
        return ResponseEntity.ok(jobSharedService.getShareBySharedId(sharedId));
    }

    @Operation(
            summary = "share job"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @PostMapping("")
    public ResponseEntity<JobSharedRes> addJobShare(@RequestBody JobShareReq req){
        return new ResponseEntity<>(jobSharedService.createShareJob(req), HttpStatus.CREATED);
    }

    @Operation(
            summary = "update status of shared job"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @PatchMapping("/{jobSharedId}")
    public ResponseEntity<JobSharedRes> updateStatus(@PathVariable long jobSharedId, @RequestParam Constants.JobStatus status){
        return ResponseEntity.ok(jobSharedService.updateJobStatus(jobSharedId, status));
    }

    @Operation(
            summary = "delete shared job"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @DeleteMapping("/{jobSharedId}")
    public ResponseEntity<String> deleteJobShare(@PathVariable long jobSharedId){
        jobSharedService.deleteShared(jobSharedId);
        return ResponseEntity.ok("Job share deleted successfully");
    }
}
