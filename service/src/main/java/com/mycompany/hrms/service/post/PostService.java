package com.mycompany.hrms.service.post;

import com.mycompany.hrms.data.dtos.post.response.*;
import com.mycompany.hrms.data.entity.post.Post;
import com.mycompany.hrms.data.entity.post.PostComments;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.post.PostCommentsRepo;
import com.mycompany.hrms.data.repository.post.PostRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.data.dtos.post.request.CommentReq;
import com.mycompany.hrms.data.dtos.post.request.CreatePost;
import com.mycompany.hrms.data.dtos.post.request.DeletePost;
import com.mycompany.hrms.data.dtos.post.request.EditCommentReq;
import com.mycompany.hrms.service.exception.BadRequestException;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import com.mycompany.hrms.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements IPostService{

    private static final String USER_NOT_FOUND = "User not found";
    private static final String POST_NOT_FOUND = "Post not found";

    private final UsersRepo usersRepo;
    private final PostRepo postRepo;
    private final ModelMapper modelMapper;
    private final PostCommentsRepo postCommentsRepo;
    private final NotificationService notificationService;

    public PostService(UsersRepo usersRepo,
                       PostRepo postRepo,
                       ModelMapper modelMapper,
                       PostCommentsRepo postCommentsRepo,
                       NotificationService notificationService) {
        this.usersRepo = usersRepo;
        this.postRepo = postRepo;
        this.modelMapper = modelMapper;
        this.postCommentsRepo = postCommentsRepo;
        this.notificationService = notificationService;
    }

    public void createPost(CreatePost createPost){
        Users user = usersRepo.findById(createPost.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author of the post does not exist"));
        Post post = modelMapper.map(createPost, Post.class);
        post.setAuthor(user);

        if(!createPost.getMentions().isEmpty()){
            List<Users> mentionedUsers = new ArrayList<>(usersRepo.findAllById(createPost.getMentions()));
            mentionedUsers.remove(user);
            for(Users mentioned : mentionedUsers){
                post.addMention(mentioned);
            }

            notificationService.addNotification(mentionedUsers, "MENTION", user.getName());
        }

        postRepo.save(post);
    }

    public List<PostResponse> getAllPost(Pageable pageable, long userId){
        return postRepo.findAllByIsDeletedFalse(pageable).stream().map(val -> {
            PostResponse res = modelMapper.map(val, PostResponse.class);
            res.setLikedByMe(postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId()));
            return res;
        }).toList();
    }

    public List<PostResponse> getPostByTags(Pageable pageable, String tags, long userId){
        return postRepo.findAllByTagsContainingAndIsDeletedFalse(tags, pageable).stream().map(val -> {
            PostResponse res = modelMapper.map(val, PostResponse.class);
            res.setLikedByMe(postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId()));
            return res;
        }).toList();
    }

    public List<PostResponse> getAllMyPost(Pageable pageable, long userId){
        return postRepo.findAllByAuthor_UserIdAndIsDeletedFalse(userId, pageable)
                .stream()
                .map(val -> {
            PostResponse res = modelMapper.map(val, PostResponse.class);
            res.setLikedByMe(postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId()));
            return res;
        }).toList();
    }

    public List<PostResponse> getMentionedPost(Pageable pageable, long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        return postRepo.findAllByMentionsContainsAndIsDeletedFalse(user, pageable)
                .stream()
                .map(val -> {
                    PostResponse res = modelMapper.map(val, PostResponse.class);
                    res.setLikedByMe(postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId()));
                    return res;
                }).toList();
    }

    public List<PostResponse> getAllMyPostDateFiltered(long userId, Pageable pageable , ZonedDateTime startDate, ZonedDateTime endDate){
        return postRepo.findAllByAuthor_UserIdAndIsDeletedFalse(userId, pageable)
                .stream()
                .filter(val -> val.getCreatedAt().isAfter(startDate) && val.getCreatedAt().isBefore(endDate))
                .map(val -> {
                    PostResponse res = modelMapper.map(val, PostResponse.class);
                    res.setLikedByMe(postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId()));
                    return res;
                }).toList();
    }

    public List<PostResponse> getFilteredPost(Pageable pageable, long userId) {

        boolean isEmp = usersRepo.findById(userId)
                .orElseThrow(() -> new RequestRejectedException(USER_NOT_FOUND))
                .getRole()
                .getName().equals("Employee");

        boolean isManager = !isEmp;

        return postRepo.findAllByIsDeletedFalse(pageable)
                .stream()
                .filter(val -> !isEmp || val.isVisibleToEmp())
                .filter(val -> !isManager || val.isVisibleToManager())
                .map(val -> {
                    PostResponse res = modelMapper.map(val, PostResponse.class);
                    res.setLikedByMe(
                            postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId())
                    );
                    return res;
                })
                .toList();
    }

    public List<PostResponse> getPostByStartDateAndEndDateFiltered(long userId, ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable){
        boolean isEmp = usersRepo.findById(userId)
                .orElseThrow(() -> new RequestRejectedException(USER_NOT_FOUND))
                .getRole()
                .getName().equals("Employee");

        boolean isManager = !isEmp;

        return postRepo.findAllBetweenStartDateAndEndDate(startDate, endDate, pageable)
                .stream()
                .filter(val -> !isEmp || val.isVisibleToEmp())
                .filter(val -> !isManager || val.isVisibleToManager())
                .map(val -> {
            PostResponse res = modelMapper.map(val, PostResponse.class);
            res.setLikedByMe(postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId()));
            return res;
        }).toList();
    }

    public List<PostResponse> getPostByStartDateAndEndDate(long userId, ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable){
        return postRepo.findAllBetweenStartDateAndEndDate(startDate, endDate, pageable).stream().map(val -> {
            PostResponse res = modelMapper.map(val, PostResponse.class);
            res.setLikedByMe(postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId()));
            return res;
        }).toList();
    }

    public List<PostResponse> getDeletedPostByUserId(long userId, Pageable pageable){
        return postRepo.findAllDeletedPostByUser(userId, pageable).stream().map(val -> {
            PostResponse res = modelMapper.map(val, PostResponse.class);
            res.setLikedByMe(postRepo.likeExistsOnPostByUserIdAndPostId(userId, val.getPostId()));
            return res;
        }).toList();
    }

    public List<PostLikeRes> getAllPostLikes(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Users> usersPage = postRepo.findPostLikes(postId, pageable);
        return usersPage.stream().map(val -> modelMapper.map(val, PostLikeRes.class)).toList();
    }

    public List<CommentsRes> getAllPostComments(Long postId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PostComments> comments = postCommentsRepo.findAllByPost_PostIdAndIsDeletedFalse(postId, pageable);
        return comments.stream().map(val -> modelMapper.map(val, CommentsRes.class)).toList();
    }

    public GetPostData getPostData(long postId){
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post was not found"));
        if(post.isDeleted())
            throw new BadRequestException("Post is deleted");
        return modelMapper.map(post, GetPostData.class);
    }

    public ProfilePostData getProfilePostData(long userId){
        Users user = usersRepo.findById(userId).
            orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        LikePostCount count = postRepo.findLikeAndPostCountByUserId(userId);

        ProfilePostData data = new ProfilePostData();
        data.setPostCount(count.getPostCount());
        data.setLikeCount(count.getLikeCount());
        data.setProfile(modelMapper.map(user, UserProfileData.class));
        data.setMentionsCount(postRepo.findMentionCountByUserId(userId));

        return data;
    }

    @Transactional
    public GetPostData updatePost(long postId, long userId, CreatePost update){
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND));
        if(post.getAuthor().getUserId() != userId)
            throw new BadRequestException("Post is not created by you");
        Users user = usersRepo.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        modelMapper.map(update, post);
        if(!update.getMentions().isEmpty()){
            post.getMentions().clear();
            List<Users> mentioned = new ArrayList<>(usersRepo.findAllById(update.getMentions()));
            mentioned.remove(user);
            for(Users u : mentioned){
                post.addMention(u);
            }
        }
        return modelMapper.map(post, GetPostData.class);
    }

    @Transactional
    public CommentsRes addComment(CommentReq comment){
        Post post = postRepo.findById(comment.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND));

        Users commentedBy = usersRepo.findById(comment.getCommentedById())
                .orElseThrow(() -> new ResourceNotFoundException("Commented by user not found"));

        PostComments newComment = new PostComments();
        newComment.setCommentText(comment.getCommentText());
        newComment.setCommentedAt(comment.getCommentedAt());
        newComment.setCommentedBy(commentedBy);
        newComment.setPost(post);
        post.setCommentCount(post.getCommentCount()+1);
        return modelMapper.map(postCommentsRepo.save(newComment), CommentsRes.class);
    }

    @Transactional
    public void addLike(long postId, long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND));
        user.addLike(post);
        post.addLike(user);
        post.setLikeCount(post.getLikeCount()+1);
    }

    @Transactional
    public void removeLike(long postId, long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND));
        user.getPostLikes().remove(post);
        post.getLikes().remove(user);
        post.setLikeCount(post.getLikeCount()-1);
    }

    @Transactional
    public void editComment(EditCommentReq req){
        PostComments comments = postCommentsRepo.findById(req.getCommentId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if(comments.getCommentedBy().getUserId() != req.getEditedBy())
            throw new BadRequestException("Comment is not made by you");
        comments.setCommentText(req.getCommentText());
        comments.setCommentedAt(req.getUpdatedAt());
    }

    @Transactional
    public void deleteComment(long commentedBy, long commentId){
        PostComments comments = postCommentsRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if(comments.getCommentedBy().getUserId() != commentedBy)
            throw new BadRequestException("Comment is not made by you");
        comments.setDeleted(true);
        Post post = comments.getPost();
        post.setCommentCount(post.getCommentCount()-1);
    }

    @Transactional
    public void restorePost(DeletePost req){
        Post post = postRepo.findById(req.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND));
        if(post.getAuthor().getUserId() != req.getDeletedById())
            throw new BadRequestException("Post is not shared by you");
        post.setDeleted(false);
    }

    @Transactional
    public void deletePost(DeletePost req){
        Post post = postRepo.findById(req.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND));
        if(post.getAuthor().getUserId() != req.getDeletedById())
            throw new BadRequestException("Post is not shared by you");
        post.setDeleted(true);
    }
}
