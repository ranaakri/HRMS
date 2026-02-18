package com.mycompany.hrms.data.entity.post;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.hrms.data.entity.user.Users;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "postId")
@Entity
public class Post {

    public Post(){
        this.createdAt = ZonedDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @Column(nullable = false)
    private String title;

    private String description;

    private String tags;

    @Column(nullable = false)
    private char postType;

    @Column(nullable = false)
    private int likeCount = 0;

    private int commentCount = 0;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    private String imagePath;

    private String publicId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private boolean isVisibleToEmp = true;

    @Column(nullable = false)
    private boolean isVisibleToManager = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private Users author;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<PostComments> comments;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "PostLikes",
            joinColumns = {@JoinColumn(name = "postId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")}
    )
    private Set<Users> likes = new HashSet<>();

    public void addLike(Users users) {
        this.likes.add(users);
        users.getPostLikes().add(this);
    }

    public Set<Users> getLikes() {
        return likes;
    }

    public void setLikes(Set<Users> likes) {
        this.likes = likes;
    }

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

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public Set<PostComments> getComments() {
        return comments;
    }

    public void setComments(Set<PostComments> comments) {
        this.comments = comments;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}