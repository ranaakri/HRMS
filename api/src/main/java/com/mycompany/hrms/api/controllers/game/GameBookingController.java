package com.mycompany.hrms.api.controllers.game;

import com.mycompany.hrms.service.dtos.game.request.BookGameSlot;
import com.mycompany.hrms.service.dtos.game.response.UserPriorityRes;
import com.mycompany.hrms.service.game.IBookingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game/bookings")
public class GameBookingController {

    private final IBookingService bookingService;

    @Autowired
    public GameBookingController(IBookingService bookingService){
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Book a new game slot"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PostMapping("")
    public ResponseEntity<Void> bookSlot(@RequestBody BookGameSlot bookGameSlot){
        bookingService.bookSlot(bookGameSlot.getSlotId(), bookGameSlot.getRequestedBy(), bookGameSlot.getUserIds());
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
