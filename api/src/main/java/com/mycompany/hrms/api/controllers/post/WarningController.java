package com.mycompany.hrms.api.controllers.post;

import com.mycompany.hrms.service.dtos.post.request.CommentWarningReq;
import com.mycompany.hrms.service.dtos.post.request.PostWarningReq;
import com.mycompany.hrms.service.dtos.post.response.GetPostData;
import com.mycompany.hrms.service.dtos.post.response.PostResponse;
import com.mycompany.hrms.service.post.IWarningService;
import com.mycompany.hrms.service.post.WarningService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post/warning")
public class WarningController {

    private final IWarningService warningService;

    @Autowired
    public WarningController(IWarningService warningService){
        this.warningService = warningService;
    }

    @Operation(
            summary = "Get all deleted post by hr"
    )
    @GetMapping("/hr/{userId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<List<PostResponse>> getAllDeletedPostByHr(@PathVariable long userId){
        return new ResponseEntity<>(warningService.getWarnedPost(userId),HttpStatus.OK);
    }

    @Operation(
            summary = "Delete post with warning"
    )
    @DeleteMapping("/post/{postId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<Void> deletePostWithWarning(@PathVariable long postId, @RequestBody PostWarningReq warningReq){
        warningService.deletePost(postId, warningReq.getWarnedBy(), warningReq.getReason(), warningReq.getTime());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete comment with warning"
    )
    @DeleteMapping("/comment/{commentId}")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseEntity<Void> deleteCommentWithWarning(@PathVariable long commentId, @RequestBody CommentWarningReq warningReq){
        warningService.deleteComment(commentId, warningReq.getWarnedBy(), warningReq.getReason(), warningReq.getTime());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
