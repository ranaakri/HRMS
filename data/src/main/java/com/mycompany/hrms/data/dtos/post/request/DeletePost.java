package com.mycompany.hrms.data.dtos.post.request;

import jakarta.validation.constraints.NotNull;

public class DeletePost {

    @NotNull
    private long postId;

    @NotNull
    private long deletedById;

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getDeletedById() {
        return deletedById;
    }

    public void setDeletedById(long deletedById) {
        this.deletedById = deletedById;
    }
}
