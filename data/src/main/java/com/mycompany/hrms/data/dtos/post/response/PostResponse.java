package com.mycompany.hrms.data.dtos.post.response;

import java.time.ZonedDateTime;
import java.util.Set;

public class PostResponse {

    private long postId;

    private String title;

    private String description;

    private String tags;

    private char postType;

    private int likeCount = 0;

    private ZonedDateTime createdAt;

    private String imagePath;

    private Set<MentionedUser> mentions;

    private boolean isDeleted = false;

    private boolean isVisibleToEmp = true;

    private boolean isVisibleToManager = true;

    private PostedByUser author;

    private long commentCount;

    boolean isLikedByMe;

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public PostedByUser getAuthor() {
        return author;
    }

    public void setAuthor(PostedByUser author) {
        this.author = author;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLikedByMe() {
        return isLikedByMe;
    }

    public void setLikedByMe(boolean likedByMe) {
        isLikedByMe = likedByMe;
    }

    public Set<MentionedUser> getMentions() {
        return mentions;
    }

    public void setMentions(Set<MentionedUser> mentions) {
        this.mentions = mentions;
    }
}
