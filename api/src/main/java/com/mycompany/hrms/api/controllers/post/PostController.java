package com.mycompany.hrms.api.controllers.post;

import com.mycompany.hrms.service.dtos.post.request.CreatePost;
import com.mycompany.hrms.service.dtos.post.response.PostResponse;
import com.mycompany.hrms.service.post.IPostService;
import io.swagger.v3.oas.annotations.Operation;
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
            summary = "add new post"
    )
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    public ResponseEntity<Void> createPost(@RequestBody CreatePost createPost){
        postService.createPost(createPost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all Post"
    )
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('HR', 'Manager', 'Employee')")
    public ResponseEntity<List<PostResponse>> getAllPost(@RequestParam int page){
        Pageable pageable = PageRequest.of(page, 1, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postService.getAllPost(pageable));
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
            summary = "Remove like from post"
    )
    @PreAuthorize("hasAnyAuthority('HR', 'Employee', 'Manager')")
    @DeleteMapping("/like/post/{postId}/user/{userId}")
    public ResponseEntity<Void> removeLikeFormPost(@PathVariable long postId, @PathVariable long userId){
        postService.removeLike(postId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
