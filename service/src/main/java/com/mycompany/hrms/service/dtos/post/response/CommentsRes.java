package com.mycompany.hrms.service.dtos.post.response;

import java.time.ZonedDateTime;

public class CommentsRes {
    public long commentId;

    public String commentText;

    public ZonedDateTime commentedAt;

    private PostLikeRes commentedBy;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public ZonedDateTime getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(ZonedDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }

    public PostLikeRes getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(PostLikeRes commentedBy) {
        this.commentedBy = commentedBy;
    }
}
