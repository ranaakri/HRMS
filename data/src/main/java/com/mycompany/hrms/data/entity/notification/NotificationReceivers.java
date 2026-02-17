package com.mycompany.hrms.data.entity.notification;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "receiverId")
@Entity
public class NotificationReceivers {

    public NotificationReceivers(){
        isRead = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notificationId")
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @Column(nullable = false)
    private boolean isRead;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }
}
