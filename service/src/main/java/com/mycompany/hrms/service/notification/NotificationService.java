package com.mycompany.hrms.service.notification;

import com.mycompany.hrms.data.entity.notification.ActionNotification;
import com.mycompany.hrms.data.entity.notification.Notification;
import com.mycompany.hrms.data.entity.notification.NotificationReceivers;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.notifications.ActionNotificationRepo;
import com.mycompany.hrms.data.repository.notifications.NotificationReceiversRepo;
import com.mycompany.hrms.data.repository.notifications.NotificationRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.notification.response.NotificationRes;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService implements INotificationService{

    private final NotificationRepo notificationRepo;
    private final NotificationReceiversRepo receiversRepo;
    private final UsersRepo usersRepo;
    private final ActionNotificationRepo actionNotificationRepo;

    @Autowired
    public NotificationService(NotificationRepo notificationRepo, NotificationReceiversRepo notificationReceiversRepo, UsersRepo usersRepo, ActionNotificationRepo actionNotificationRepo){
        this.notificationRepo = notificationRepo;
        this.receiversRepo = notificationReceiversRepo;
        this.usersRepo = usersRepo;
        this.actionNotificationRepo = actionNotificationRepo;
    }

    public void addNotification(List<Long> userIds, String action, String tail){
        ActionNotification notificationData = actionNotificationRepo.findActionNotificationsByAction(action);
        List<Users> users = usersRepo.findAllById(userIds);
        Notification notification = new Notification();
        notification.setTitle(notificationData.getNotificationTitle());
        notification.setDescription(notificationData.getNotificationDescription()+" "+tail);

        Notification saved = notificationRepo.save(notification);
        for(Users user : users){
            NotificationReceivers receivers = new NotificationReceivers();
            receivers.setNotification(saved);
            receivers.setUser(user);
            receiversRepo.save(receivers);
        }

    }

    public List<NotificationRes> getUnreadNotifications(long userId){
        List<NotificationReceivers> received = receiversRepo.findNotificationReceiversByUser_UserIdAndIsRead(userId, false);
        List<NotificationRes> response = new ArrayList<>();
        for(NotificationReceivers r : received){
            NotificationRes res = new NotificationRes();
            res.setTitle(r.getNotification().getTitle());
            res.setDescription(r.getNotification().getDescription());
            res.setRead(r.isRead());
            res.setReceiverId(r.getReceiverId());
            res.setTime(r.getNotification().getTime());
            response.add(res);
        }
        return response;
    }

    @Transactional
    public void changeStatus(long receiverId){
        NotificationReceivers receivers = receiversRepo.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid notification id"));
        receivers.setRead(true);
    }

    @Transactional
    public void changeStateByUserIdAndNotificationId(long userId, long notificationId){
        NotificationReceivers receivers = receiversRepo.findNotificationReceiversByNotification_NotificationIdAndUser_UserId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid notification id"));
        receivers.setRead(true);
    }
}
