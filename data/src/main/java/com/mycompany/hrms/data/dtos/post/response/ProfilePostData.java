package com.mycompany.hrms.data.dtos.post.response;

public class ProfilePostData {

    private UserProfileData profile;

    private long mentionsCount;
    private long likeCount;
    private long postCount;

    public UserProfileData getProfile() {
        return profile;
    }

    public void setProfile(UserProfileData profile) {
        this.profile = profile;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }

    public long getMentionsCount() {
        return mentionsCount;
    }

    public void setMentionsCount(long mentionsCount) {
        this.mentionsCount = mentionsCount;
    }
}
