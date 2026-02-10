package com.mycompany.hrms.api.controllers.travel;

import com.mycompany.hrms.api.response.ApiResponse;
import com.mycompany.hrms.service.dtos.travel.request.TravelDetailsReq;
import com.mycompany.hrms.service.dtos.travel.request.UpdateTravelDetailsReq;
import com.mycompany.hrms.service.dtos.travel.response.TravelDetailsRes;
import com.mycompany.hrms.service.travel.ITravelDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel")
public class TravelDetailsController {

    private final ITravelDetailsService travelDetailsService;

    @Autowired
    public TravelDetailsController(ITravelDetailsService travelDetailsService){
        this.travelDetailsService = travelDetailsService;
    }

    @Operation(
            summary = "Get travel details by id"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @GetMapping("/{travelId}")
    public ResponseEntity<ApiResponse<TravelDetailsRes>> getTravelDetailsById(@PathVariable long travelId){
        return new ResponseEntity<>(ApiResponse.success(travelDetailsService.getTravelDetailsId(travelId), "Travel details fetched successfully"), HttpStatus.OK);
    }

    @Operation(
            summary = "Get Travel details by user id",
            description = "Get all travel details created by user"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'employee')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TravelDetailsRes>>> getTravelDetailsByUserId(@PathVariable long userId){
        return ResponseEntity.ok(ApiResponse.success(travelDetailsService.getTravelDetailsByUserId(userId), "Travel details list fetched successfully"));
    }

    @Operation(
            summary = "Create new travel details",
            description = "Create new travel details"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<TravelDetailsRes>> addTravelDetails(@Valid @RequestBody TravelDetailsReq req){
        return new ResponseEntity<>(ApiResponse.created(travelDetailsService.addTravelDetail(req), "New Travel details created successfully"), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update travel details"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @PutMapping("/{travelId}")
    public ResponseEntity<ApiResponse<TravelDetailsRes>> updateTravelDetails(@PathVariable long travelId, @RequestBody UpdateTravelDetailsReq travelDetailsReq){
        return ResponseEntity.ok(ApiResponse.success(travelDetailsService.updateTravelDetails(travelId, travelDetailsReq), "Travel details updated successfully"));
    }

    @Operation(
            summary = "Delete travel details using id"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @DeleteMapping("/{travelId}")
    public ResponseEntity<ApiResponse<Void>> deleteTravelDetails(@PathVariable long travelId){
        travelDetailsService.deleteTravelDetails(travelId);
        return new ResponseEntity<>(ApiResponse.deleted("Travel detail deleted successfully"), HttpStatus.OK);
    }
}
