package com.mycompany.hrms.service.notification;

import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.dtos.notification.response.NotificationRes;

import java.util.List;

public interface INotificationService {
    void addNotification(List<Users> userIds, String action, String tail);
    List<NotificationRes> getUnreadNotifications(long userId);
    void changeStatus(long receiverId);
    void changeStateByUserIdAndNotificationId(long userId, long notificationId);
}