package com.mycompany.hrms.data.dtos.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public class EditCommentReq {

    @NotNull
    private long commentId;

    @NotNull
    private long editedBy;

    @NotBlank
    private String commentText;

    private ZonedDateTime updatedAt;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(long editedBy) {
        this.editedBy = editedBy;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
