package com.mycompany.hrms.api.controllers.travel;

import com.mycompany.hrms.api.response.ApiResponse;
import com.mycompany.hrms.service.dtos.travel.request.AddTravelingUserReq;
import com.mycompany.hrms.service.dtos.travel.request.UpdateTravelBalance;
import com.mycompany.hrms.service.dtos.travel.response.TravelingUserRes;
import com.mycompany.hrms.service.travel.ITravelingUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel/traveling-user")
public class TravelingUserController {

    private final ITravelingUserService travelingUserService;

    @Autowired
    public TravelingUserController(ITravelingUserService travelingUserService){
        this.travelingUserService = travelingUserService;
    }

    @Operation(
            summary = "Get all traveling users by travel id"
    )
    @PreAuthorize("hasAnyAuthority('HR','Manager','Employee')")
    @GetMapping("/{travelId}")
    public ResponseEntity<List<TravelingUserRes>> getTravelingUserList(@PathVariable long travelId){
        return ResponseEntity.ok(travelingUserService.getTravelingUsers(travelId));
    }

    @Operation(
            summary = "Assign users to travel with balance",
            description = "Assign users to travel with balance"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Void>> addTravelingUser(@Valid @RequestBody AddTravelingUserReq travelingUserReq){
        travelingUserService.assignUserToTravel(travelingUserReq);
        return ResponseEntity.ok(ApiResponse.successMsg("Users assigned to travel successfully"));
    }

    @Operation(
            summary = "Update Balance of traveling user",
            description = "Update balance of traveling user by traviling user id"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @PatchMapping("/{travelingUserId}")
    public ResponseEntity<ApiResponse<Void>> updateTravelingUserBalance(@Valid @PathVariable long travelingUserId, @RequestBody UpdateTravelBalance travelBalance){
        travelingUserService.updateAssignedBudget(travelingUserId, travelBalance.getUpdatedBalance());
        return ResponseEntity.ok(ApiResponse.successMsg("Traveling balance of user is updated"));
    }

    @Operation(
            summary = "Remove user form travelling users",
            description = "Using traveling user id"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @DeleteMapping("/{travelingUserId}")
    public ResponseEntity<ApiResponse<Void>> deleteTravelingUser(@PathVariable long travelingUserId){
        travelingUserService.deleteUserFromTravel(travelingUserId);
        return ResponseEntity.ok(ApiResponse.successMsg("Traveling user removed successfully"));
    }
}
