package com.mycompany.hrms.service.post;

import com.mycompany.hrms.service.dtos.post.request.CreatePost;
import com.mycompany.hrms.service.dtos.post.response.PostResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostService {
    void createPost(CreatePost createPost);
    List<PostResponse> getAllPost(Pageable pageable);
    void removeLike(long postId, long userId);
    void addLike(long postId, long userId);
}
