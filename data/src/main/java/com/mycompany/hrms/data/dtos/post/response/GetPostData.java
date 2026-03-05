package com.mycompany.hrms.data.dtos.post.response;


import java.util.Set;

public class GetPostData {

    private long userId;

    private String title;

    private String description;

    private String tags;

    private String imagePath;

    private char postType;

    private Set<MentionedUser> mentions;

    private String publicId;

    private boolean isVisibleToEmp = true;

    private boolean isVisibleToManager = true;

    private long authorId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public Set<MentionedUser> getMentions() {
        return mentions;
    }

    public void setMentions(Set<MentionedUser> mentions) {
        this.mentions = mentions;
    }
}
