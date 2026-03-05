package com.mycompany.hrms.api.controllers.job;

import com.mycompany.hrms.data.constant.Constants;
import com.mycompany.hrms.data.dtos.job.request.ReferralJobReq;
import com.mycompany.hrms.data.dtos.job.response.JobRes;
import com.mycompany.hrms.data.dtos.job.response.ReferralJobRes;
import com.mycompany.hrms.service.jobs.IReferralService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/referral")
public class ReferralController {

    private final IReferralService referralService;

    @Autowired
    public ReferralController(IReferralService referralService){
        this.referralService = referralService;
    }

    @Operation(
            summary = "Get list of referral by user id"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReferralJobRes>> getReferralsByUserId(@PathVariable long userId){
        return ResponseEntity.ok(referralService.getListOfReferralsByUserId(userId));
    }

    @Operation(
            summary = "Get list jobs for cv review by user id"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @GetMapping("/cv-review/user/{userId}")
    public ResponseEntity<List<JobRes>> getListOfJobsForCvReview(@PathVariable long userId){
        return ResponseEntity.ok(referralService.getJobsForCvReviews(userId));
    }

    @Operation(
            summary = "Get list jobs for cv review by user id"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @GetMapping("/cv-review/referrals/job/{jobId}/user/{userId}")
    public ResponseEntity<List<ReferralJobRes>> getListOfReferralsForCvReview(@PathVariable long jobId, @PathVariable long userId){
        return ResponseEntity.ok(referralService.getReferralsForCvReview(jobId, userId));
    }

    @Operation(
            summary = "Get referrals by job id"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ReferralJobRes>> getReferralsByJobId(@PathVariable long jobId){
        return ResponseEntity.ok(referralService.getListOfReferralsByJoId(jobId));
    }

    @Operation(
            summary = "Create Referrals"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PostMapping("")
    public ResponseEntity<ReferralJobRes> createReferral(@RequestBody ReferralJobReq req){
        return ResponseEntity.ok(referralService.createReferral(req));
    }

    @Operation(
            summary = "Change status"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee' )")
    @PatchMapping("/{referralId}")
    public ResponseEntity<ReferralJobRes> updateStatus(@PathVariable long referralId, @RequestParam Constants.JobStatus status){
        return ResponseEntity.ok(referralService.updateStatus(referralId, status));
    }

    @Operation(
            summary = "delete cv"
    )
    @PreAuthorize("hasAnyAuthority('Hr', 'Employee', 'Manager')")
    @DeleteMapping("/cv/{publicId}")
    public ResponseEntity<Void> deleteCv(@PathVariable String publicId){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
