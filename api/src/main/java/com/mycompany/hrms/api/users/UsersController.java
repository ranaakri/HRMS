package com.mycompany.hrms.api.users;

import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileDto;
import com.mycompany.hrms.service.dtos.users.request.UserProfileCreate;
import com.mycompany.hrms.service.dtos.users.response.UpdatedUserProfileDto;
import com.mycompany.hrms.service.dtos.users.response.UserProfileCreated;
import com.mycompany.hrms.service.dtos.users.response.UserProfileDto;
import com.mycompany.hrms.service.users.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final IUserService usersService;

    @Autowired
    public UsersController(IUserService usersService){
        this.usersService = usersService;
    }

    @Operation(
            summary = "Get user profile by id",
            description = "Get user profile by using user id"
    )
    @GetMapping("/id/{userId}")
    @PreAuthorize("hasAnyAuthority('Employee')")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable long userId){
        return new ResponseEntity<>(usersService.getUserProfileById(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Get user profile by email",
            description = "Get user profile by employee email id"
    )
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyAuthority('Employee')")
    public ResponseEntity<UserProfileDto> getUserByEmail(@PathVariable String email){
        return new ResponseEntity<>(usersService.getUserProfileByEmail(email), HttpStatus.OK);
    }

    @Operation(
            summary = "Create new user profile",
            description = "Create new user profile"
    )
    @PostMapping("/")
    public ResponseEntity<UserProfileCreated> createUserProfile(@Valid @RequestBody UserProfileCreate userProfileCreate){
        return new ResponseEntity<>(usersService.createUserProfile(userProfileCreate), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update user profile",
            description = "Update user profile"
    )
    @PatchMapping("/{userId}")
    public ResponseEntity<UpdatedUserProfileDto> updateUserProfile(@PathVariable long userId,@Valid @RequestBody UpdateUserProfileDto updateUserProfile) {
        return new ResponseEntity<>(usersService.updateUserProfile(userId, updateUserProfile), HttpStatus.OK);
    }

}
