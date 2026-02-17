package com.mycompany.hrms.data.repository.notifications;

import com.mycompany.hrms.data.entity.notification.ActionNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionNotificationRepo extends JpaRepository<ActionNotification, Long> {
    ActionNotification findActionNotificationsByAction(String action);
}