package com.mycompany.hrms.data.entity.post;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "commentId")
@Entity
public class PostComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long commentId;

    @Column(nullable = false)
    public String commentText;

    @Column(nullable = false)
    public ZonedDateTime commentedAt;

    @Column(nullable = false)
    public boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentedBy")
    private Users commentedBy;

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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Users getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(Users commentedBy) {
        this.commentedBy = commentedBy;
    }
}
