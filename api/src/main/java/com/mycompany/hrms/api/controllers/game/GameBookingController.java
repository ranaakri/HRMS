package com.mycompany.hrms.api.controllers.game;

import com.mycompany.hrms.data.entity.game.SlotRequest;
import com.mycompany.hrms.service.dtos.game.request.BookGameSlot;
import com.mycompany.hrms.service.dtos.game.request.DeleteSlotRequest;
import com.mycompany.hrms.service.dtos.game.response.UserPriorityRes;
import com.mycompany.hrms.service.dtos.travel.response.CreatedByUser;
import com.mycompany.hrms.service.game.IBookingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-bookings")
public class GameBookingController {

    private final IBookingService bookingService;

    @Autowired
    public GameBookingController(IBookingService bookingService){
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Get booking status"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @GetMapping("/get/user/{userId}/slot/{slotId}")
    public ResponseEntity<SlotRequest.RequestStatus> getBookingStatus(@PathVariable long userId, @PathVariable long slotId){
        return ResponseEntity.ok(bookingService.getBookingStatus(slotId,userId));
    }

    @Operation(
            summary = "check if user has booking or not"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @GetMapping("/check-booking/user/{userId}/slot/{slotId}")
    public ResponseEntity<Boolean> checkBooking(@PathVariable long userId, @PathVariable long slotId){
        return ResponseEntity.ok(bookingService.checkBooking(slotId,userId));
    }

    @Operation(
            summary = "Get all booking partners"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @GetMapping("/partners/user/{userId}/slot/{slotId}")
    public ResponseEntity<List<CreatedByUser>> getBookingPartners(@PathVariable long userId, @PathVariable long slotId){
        return ResponseEntity.ok(bookingService.getBookingPartners(userId,slotId));
    }

    @Operation(
            summary = "Book a new game slot"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PostMapping("")
    public ResponseEntity<Void> bookSlot(@Valid @RequestBody BookGameSlot bookGameSlot){
        bookingService.bookSlot(bookGameSlot.getSlotId(), bookGameSlot.getRequestedBy(), bookGameSlot.getUserIds());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete booking"
    )
    @DeleteMapping("")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<Void> cancelSlot(@RequestBody DeleteSlotRequest deleteSlotRequest){
        bookingService.cancelSlotRequest(deleteSlotRequest.getSlotId(), deleteSlotRequest.getRequestedBy());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Get List of booking of slot"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    @GetMapping("/{slotId}")
    public ResponseEntity<List<UserPriorityRes>> getBookingList(@PathVariable long slotId){
        return ResponseEntity.ok(bookingService.getPriorityList(slotId));
    }
}
