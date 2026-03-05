package com.mycompany.hrms.service.post;

import com.mycompany.hrms.data.entity.post.Post;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.post.PostRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class SchedulePostService {

    private final PostRepo postRepo;
    private final UsersRepo usersRepo;
    private final NotificationService notificationService;

    @Autowired
    public SchedulePostService(PostRepo postRepo,
                               UsersRepo usersRepo,
                               NotificationService notificationService){
        this.postRepo = postRepo;
        this.usersRepo = usersRepo;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void makeBirthdayPost(){
        List<Users> birthday = usersRepo.findUserWithBirthday();

        for(Users u : birthday){
            Post post = new Post();

            post.setTitle("Happy Birthday " + u.getName());
            post.setDescription("Happy birthday " + u.getName() + "!! Best wishes form us");
            post.setTags("#Birthday");
            post.setPostType('I');
            post.setCreatedAt(ZonedDateTime.now());
            post.setImagePath(u.getProfileUrl());
            post.setPublicId(null);
            post.setAuthor(null);
            post.addMention(u);
            postRepo.save(post);
        }

        notificationService.addNotification(birthday, "SYS_MENTION_BIR", "");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void makeJoiningAnniversaryPost() {

        List<Users> anniversaryUsers = usersRepo.findUserWithJoiningAnniversary();

        for (Users u : anniversaryUsers) {

            Post post = new Post();

            post.setTitle("Happy Work Anniversary " + u.getName());
            post.setDescription("Congratulations " + u.getName() +
                    " on your work anniversary! Thank you for being an amazing part of our journey.");
            post.setTags("#WorkAnniversary");
            post.setPostType('I');
            post.setCreatedAt(ZonedDateTime.now());
            post.setImagePath(u.getProfileUrl());
            post.setPublicId(null);
            post.setAuthor(null);
            post.addMention(u);
            postRepo.save(post);
        }

        notificationService.addNotification(anniversaryUsers, "SYS_MENTION_ANI", "");
    }
}
