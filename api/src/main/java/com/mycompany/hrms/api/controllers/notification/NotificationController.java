package com.mycompany.hrms.api.controllers.notification;

import com.mycompany.hrms.service.dtos.notification.response.NotificationRes;
import com.mycompany.hrms.service.notification.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final INotificationService notificationService;

    @Autowired
    public NotificationController(INotificationService notificationService){
        this.notificationService = notificationService;
    }

    @Operation(
            summary = "Fetch unread notification by user id"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<NotificationRes>> getUnreadNotifications(@PathVariable long userId){
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @Operation(
            summary = "Change status of notification to read by receiver id"
    )
    @PatchMapping("/{receiverId}")
    @PreAuthorize("hasAnyAuthority('Employee', 'HR', 'Manager')")
    public ResponseEntity<Void> checkNotificationRead(@PathVariable long receiverId){
        notificationService.changeStatus(receiverId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
