package com.mycompany.hrms.service.post;

import com.mycompany.hrms.data.entity.post.Post;
import com.mycompany.hrms.data.entity.post.PostComments;
import com.mycompany.hrms.data.entity.post.Warnings;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.post.PostCommentsRepo;
import com.mycompany.hrms.data.repository.post.PostRepo;
import com.mycompany.hrms.data.repository.post.WarningsRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.post.response.PostResponse;
import com.mycompany.hrms.service.email.EmailService;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import com.mycompany.hrms.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class WarningService implements IWarningService{

    private final PostRepo postRepo;
    private final UsersRepo usersRepo;
    private final WarningsRepo warningsRepo;
    private final PostCommentsRepo postCommentsRepo;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Autowired
    public WarningService(PostRepo postRepo,
                          UsersRepo usersRepo,
                          WarningsRepo warningsRepo,
                          PostCommentsRepo postCommentsRepo,
                          NotificationService notificationService,
                          EmailService emailService,
                          ModelMapper modelMapper){
        this.postRepo = postRepo;
        this.usersRepo = usersRepo;
        this.postCommentsRepo = postCommentsRepo;
        this.warningsRepo = warningsRepo;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    public List<PostResponse> getWarnedPost(long userId){
        List<Long> postIds = warningsRepo.findAllByEntityTypeAndWarnedBy_UserId(Warnings.EntityType.POST, userId).stream().map(Warnings::getEntityId).toList();
        List<Post> warnedPosts = postRepo.findAllById(postIds).stream().sorted(Comparator.comparing(Post::getCreatedAt)).toList();
        return warnedPosts.stream().map(val -> modelMapper.map(val, PostResponse.class)).toList();
    }

    @Transactional
    public void deletePost(long postId, long warnedById,String reason, ZonedDateTime time){
        Post deletedPost = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if(deletedPost.isDeleted())
            throw new BadRequestException("Post is already deleted");
        if(deletedPost.getAuthor() == null)
            throw new BadRequestException("Can not warn System generated post");
        Users warnedBy = usersRepo.findById(warnedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Warnings warning = new Warnings();
        warning.setWarnedBy(warnedBy);
        warning.setReason(reason);
        warning.setWarningDate(time);
        warning.setEntityType(Warnings.EntityType.POST);
        warning.setEntityId(postId);

        deletedPost.setDeleted(true);

        warningsRepo.save(warning);

        List<Users> users = new ArrayList<>();
        users.add(deletedPost.getAuthor());
        notificationService.addNotification(users, "WARN_DELETE_POST", "");
        emailService.sendWarningEmail(deletedPost.getAuthor(), reason, "Inappropriate Post warning");
    }

    @Transactional
    public void deleteComment(long commentId, long warnedById,String reason, ZonedDateTime time){
        PostComments deletedComment = postCommentsRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if(deletedComment.isDeleted())
            throw new BadRequestException("Comment is already deleted");
        Users warnedBy = usersRepo.findById(warnedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Warnings warning = new Warnings();
        warning.setWarnedBy(warnedBy);
        warning.setReason(reason);
        warning.setWarningDate(time);
        warning.setEntityType(Warnings.EntityType.COMMENT);
        warning.setEntityId(commentId);

        warningsRepo.save(warning);

        deletedComment.setDeleted(true);

        List<Users> users = new ArrayList<>();
        users.add(deletedComment.getCommentedBy());
        notificationService.addNotification(users, "WARN_DELETE_COMMENT", "");
        emailService.sendWarningEmail(deletedComment.getCommentedBy(), reason, "Inappropriate Comment warning");
    }
}
