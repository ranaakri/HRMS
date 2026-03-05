package com.mycompany.hrms.data.dtos.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreatePost {

    @NotBlank
    private String title;

    private String description;

    private String tags;

    private char postType;

    @NotBlank
    private String imagePath;

    @NotBlank
    private String publicId;

    private List<Long> mentions;

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

    public List<Long> getMentions() {
        return mentions;
    }

    public void setMentions(List<Long> mentions) {
        this.mentions = mentions;
    }
}
