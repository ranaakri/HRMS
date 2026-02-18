package com.mycompany.hrms.service.dtos.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreatePost {

    @NotNull
    @NotBlank
    @NotEmpty
    private String title;

    private String description;

    private String tags;

    @NotNull
    @NotBlank
    @NotEmpty
    private char postType;

    @NotNull
    @NotBlank
    @NotEmpty
    private String imagePath;

    @NotNull
    @NotBlank
    @NotEmpty
    private String publicId;

    @NotNull
    private boolean isVisibleToEmp = true;

    @NotNull
    private boolean isVisibleToManager = true;

    private long authorId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public char getPostType() {
        return postType;
    }

    public void setPostType(char postType) {
        this.postType = postType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public boolean isVisibleToEmp() {
        return isVisibleToEmp;
    }

    public void setVisibleToEmp(boolean visibleToEmp) {
        isVisibleToEmp = visibleToEmp;
    }

    public boolean isVisibleToManager() {
        return isVisibleToManager;
    }

    public void setVisibleToManager(boolean visibleToManager) {
        isVisibleToManager = visibleToManager;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
}
