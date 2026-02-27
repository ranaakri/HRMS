package com.mycompany.hrms.api.controllers.users;

import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileDto;
import com.mycompany.hrms.service.dtos.users.request.UpdateUserProfileHr;
import com.mycompany.hrms.service.dtos.users.request.UserProfileCreate;
import com.mycompany.hrms.service.dtos.users.response.*;
import com.mycompany.hrms.service.users.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasAnyAuthority('Employee', 'HR', 'Manager')")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable long userId){
        return new ResponseEntity<>(usersService.getUserProfileByUserId(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Get list of designations"
    )
    @GetMapping("/designations")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<List<String>> getAllDesignations(){
        return ResponseEntity.ok(usersService.getAllDesignations());
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
            summary = "Get List of users who's birthday is today"
    )
    @GetMapping("/birthday")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<List<EventRes>> getBirthdays(){
        return ResponseEntity.ok(usersService.getUsersWithBirthday());
    }

    @Operation(
            summary = "Get fav game info and upcoming slots"
    )
    @GetMapping("/game/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<FavouriteGameResponse> getFavGameSlots(@PathVariable long userId){
        return ResponseEntity.ok(usersService.getSlotsOfFavouriteGame(userId));
    }

    @Operation(
            summary = "Get list of users name and email by name"
    )
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('Employee', 'HR', 'Manager')")
    public ResponseEntity<List<UserListRes>> getUserListByName(@RequestParam String name){
        return ResponseEntity.ok(usersService.getUsersListByName(name));
    }

    @Operation(
            summary = "Get organization chart"
    )
    @GetMapping("/org/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    public ResponseEntity<List<OrgChartRes>> getOrgChart(@PathVariable long userId){
        return ResponseEntity.ok(usersService.getOrgChartList(userId));
    }

    @Operation(
            summary = "Get list of all users"
    )
    @GetMapping("/list/all")
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    public ResponseEntity<List<UserProfileDto>> getListOfUsersProfile(
            @RequestParam(required = false, defaultValue = "0")Integer page,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long department){
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(usersService.getAllUserProfiles(pageable, department));
    }

    @Operation(
            summary = "Get assign under users"
    )
    @GetMapping("/org/under/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    public ResponseEntity<List<OrgChartRes>> getAssignedUnder(@PathVariable long userId){
        return ResponseEntity.ok(usersService.getAssignedUnder(userId));
    }

    @Operation(
            summary = "Create new user profile",
            description = "Create new user profile"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PostMapping("")
    public ResponseEntity<UserProfileCreated> createUserProfile(@RequestBody UserProfileCreate userProfileCreate){
        return new ResponseEntity<>(usersService.createUserProfile(userProfileCreate), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Set Favourite game"
    )
    @PatchMapping("/fav-game/user/{userId}/game/{gameId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<Void> setFavGame(@PathVariable long userId, @PathVariable long gameId){
        usersService.makeGameFavourite(userId, gameId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Update Active status"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @PatchMapping("/user/{userId}/status/{status}")
    public ResponseEntity<Void> updateUserStatus(@PathVariable long userId, @PathVariable boolean status){
        usersService.updateActiveStatus(userId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Remove Favourite game"
    )
    @DeleteMapping("/fav-game/user/{userId}/game/{gameId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<Void> deleteFavGame(@PathVariable long userId, @PathVariable long gameId){
        usersService.removeGameFromFavourite(userId, gameId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Update user profile",
            description = "Update user profile"
    )
    @PatchMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<UpdatedUserProfileDto> updateUserProfile(@PathVariable long userId,@Valid @RequestBody UpdateUserProfileDto updateUserProfile) {
        return new ResponseEntity<>(usersService.updateUserProfile(userId, updateUserProfile), HttpStatus.OK);
    }

    @Operation(
            summary = "Update user profile"
    )
    @PreAuthorize("hasAnyAuthority('HR')")
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserProfileByHr(@PathVariable long userId, @RequestBody UpdateUserProfileHr updateUserProfileHr){
        usersService.updateUserProfile(userId, updateUserProfileHr);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
