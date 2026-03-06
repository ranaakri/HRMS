package com.mycompany.hrms.service.post;

import com.mycompany.hrms.data.dtos.post.request.CommentReq;
import com.mycompany.hrms.data.dtos.post.request.CreatePost;
import com.mycompany.hrms.data.dtos.post.request.DeletePost;
import com.mycompany.hrms.data.dtos.post.request.EditCommentReq;
import com.mycompany.hrms.data.dtos.post.response.*;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

public interface IPostService {
    void createPost(CreatePost createPost);

    List<PostResponse> getAllPost(Pageable pageable, long userId);

    void removeLike(long postId, long userId);

    void addLike(long postId, long userId);

    List<PostLikeRes> getAllPostLikes(Long postId, int page, int size);

    List<CommentsRes> getAllPostComments(Long postId, int page, int size);

    CommentsRes addComment(CommentReq comment);

    void deleteComment(long commentedBy, long commentId);

    void editComment(EditCommentReq req);

    List<PostResponse> getFilteredPost(Pageable pageable, long userId);

    List<PostResponse> getAllMyPost(Pageable pageable, long userId);

    void deletePost(DeletePost req);

    GetPostData getPostData(long postId);

    GetPostData updatePost(long postId, long userId, CreatePost update);

    List<PostResponse> getPostByStartDateAndEndDate(long userId, ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable);

    List<PostResponse> getPostByStartDateAndEndDateFiltered(long userId, ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable);

    List<PostResponse> getAllMyPostDateFiltered(long userId, Pageable pageable, ZonedDateTime startDate, ZonedDateTime endDate);

    List<PostResponse> getPostByTags(Pageable pageable, String tags, long userId);

    List<PostResponse> getMentionedPost(Pageable pageable, long userId);

    ProfilePostData getProfilePostData(long userId);

    List<PostResponse> getDeletedPostByUserId(long userId, Pageable pageable);

    void restorePost(DeletePost req);
}
