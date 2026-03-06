package com.mycompany.hrms.data.dtos.post.response;

public class LikePostCount {
    private long likeCount;
    private long postCount;

    public LikePostCount(long likeCount, long postCount){
        this.likeCount = likeCount;
        this.postCount = postCount;
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
}
