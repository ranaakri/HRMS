package com.mycompany.hrms.service.notification;

import com.mycompany.hrms.service.dtos.notification.response.NotificationRes;

import java.util.List;

public interface INotificationService {
    void addNotification(List<Long> userIds, String action, String tail);
    List<NotificationRes> getUnreadNotifications(long userId);
    void changeStatus(long receiverId);
    void changeStateByUserIdAndNotificationId(long userId, long notificationId);
}