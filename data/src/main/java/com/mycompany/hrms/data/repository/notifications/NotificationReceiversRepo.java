package com.mycompany.hrms.data.repository.notifications;

import com.mycompany.hrms.data.entity.notification.NotificationReceivers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationReceiversRepo extends JpaRepository<NotificationReceivers, Long> {
    Optional<NotificationReceivers> findNotificationReceiversByNotification_NotificationIdAndUser_UserId(long notificationNotificationId, long userUserId);

    List<NotificationReceivers> findNotificationReceiversByUser_UserIdAndIsRead(long userUserId, boolean isRead);

}