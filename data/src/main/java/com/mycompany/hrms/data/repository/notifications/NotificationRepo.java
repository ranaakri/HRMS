package com.mycompany.hrms.data.repository.notifications;

import com.mycompany.hrms.data.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
}
