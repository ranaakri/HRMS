package com.mycompany.hrms.api.controllers.post;

import com.mycompany.hrms.service.dtos.post.request.*;
import com.mycompany.hrms.service.dtos.post.response.CommentsRes;
import com.mycompany.hrms.service.dtos.post.response.GetPostData;
import com.mycompany.hrms.service.dtos.post.response.PostLikeRes;
import com.mycompany.hrms.service.dtos.post.response.PostResponse;
import com.mycompany.hrms.service.post.IPostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final IPostService postService;

    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "Get info to update post"
    )
    @GetMapping("/info/{postId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<GetPostData> getPostInfoById(@PathVariable long postId){
        return ResponseEntity.ok(postService.getPostData(postId));
    }

    @Operation(
            summary = "add new post"
    )
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<Void> createPost(@Valid @RequestBody CreatePost createPost){
        postService.createPost(createPost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all Post"
    )
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<List<PostResponse>> getAllPost(@PathVariable long userId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postService.getAllPost(pageable, userId));
    }

    @Operation(
            summary = "Get filtered post"
    )
    @GetMapping("/filtered/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    public ResponseEntity<List<PostResponse>> getFilteredPost(@PathVariable long userId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postService.getFilteredPost(pageable, userId));
    }

    @Operation(
            summary = "Get all Post which is posted by me"
    )
    @GetMapping("/my/{userId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee','Manager')")
    public ResponseEntity<List<PostResponse>> getAllPostPostedByMe(@PathVariable long userId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postService.getAllMyPost(pageable, userId));
    }

    @Operation(
            summary = "Get all Users who liked the post"
    )
    @GetMapping("/likes/{postId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    public ResponseEntity<List<PostLikeRes>> getAllPostLikes(@PathVariable long postId, @RequestParam(defaultValue = "0") int page){
        return ResponseEntity.ok(postService.getAllPostLikes(postId, page, 10));
    }

    @Operation(
            summary = "Get all Users who commented on the post"
    )
    @GetMapping("/comments/{postId}")
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    public ResponseEntity<List<CommentsRes>> getAllPostComments(@PathVariable long postId, @RequestParam(defaultValue = "0") int page){
        return ResponseEntity.ok(postService.getAllPostComments(postId, page, 10));
    }

    @Operation(
            summary = "Add Comment to the post"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PostMapping("/comment")
    public ResponseEntity<CommentsRes> addCommentToPost(@Valid @RequestBody CommentReq commentReq){
        return ResponseEntity.ok(postService.addComment(commentReq));
    }

    @Operation(
            summary = "Add like to the post"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PatchMapping("/like/post/{postId}/user/{userId}")
    public ResponseEntity<Void> addLikeToPost(@PathVariable long postId, @PathVariable long userId){
        postService.addLike(postId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Update the comment with comment id"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PutMapping("/comment")
    public ResponseEntity<Void> updateComment(@Valid @RequestBody EditCommentReq req){
        postService.editComment(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Update Post"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @PutMapping("/updatedBy/{userId}/post/{postId}")
    public ResponseEntity<GetPostData> updatePost(@PathVariable long userId, @PathVariable long postId, @Valid @RequestBody CreatePost updatePost){
        return ResponseEntity.ok(postService.updatePost(postId, userId, updatePost));
    }

    @Operation(
            summary = "Remove like from post"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @DeleteMapping("/like/post/{postId}/user/{userId}")
    public ResponseEntity<Void> removeLikeFormPost(@PathVariable long postId, @PathVariable long userId){
        postService.removeLike(postId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete comment by comment id"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @DeleteMapping("/comment")
    public ResponseEntity<Void> deleteComment(@Valid @RequestBody DeleteCommentReq deleteReq){
        postService.deleteComment(deleteReq.getCommentedById(), deleteReq.getCommentId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete post by post id"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @DeleteMapping("")
    public ResponseEntity<Void> deletePost(@Valid @RequestBody DeletePost deleteReq){
        postService.deletePost(deleteReq);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
