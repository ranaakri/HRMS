package com.mycompany.hrms.service.dtos.post.request;

import jakarta.validation.constraints.NotNull;

public class DeleteCommentReq {
    @NotNull
    private long commentedById;

    @NotNull
    private long commentId;

    public long getCommentedById() {
        return commentedById;
    }

    public void setCommentedById(long commentedById) {
        this.commentedById = commentedById;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }
}
