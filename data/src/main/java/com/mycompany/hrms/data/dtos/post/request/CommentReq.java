package com.mycompany.hrms.data.dtos.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public class CommentReq {

    @NotNull
    @NotBlank
    @NotEmpty
    public String commentText;

    @NotNull
    public Long commentedById;

    public ZonedDateTime commentedAt;

    @NotNull
    public Long postId;

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getCommentedById() {
        return commentedById;
    }

    public void setCommentedById(Long commentedById) {
        this.commentedById = commentedById;
    }

    public ZonedDateTime getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(ZonedDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
