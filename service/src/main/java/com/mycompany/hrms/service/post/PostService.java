package com.mycompany.hrms.service.post;

import com.mycompany.hrms.data.entity.post.Post;
import com.mycompany.hrms.data.entity.user.Users;
import com.mycompany.hrms.data.repository.post.PostRepo;
import com.mycompany.hrms.data.repository.users.UsersRepo;
import com.mycompany.hrms.service.dtos.post.request.CreatePost;
import com.mycompany.hrms.service.dtos.post.response.PostResponse;
import com.mycompany.hrms.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements IPostService{

    private final UsersRepo usersRepo;
    private final PostRepo postRepo;
    private final ModelMapper modelMapper;

    public PostService(UsersRepo usersRepo, PostRepo postRepo, ModelMapper modelMapper) {
        this.usersRepo = usersRepo;
        this.postRepo = postRepo;
        this.modelMapper = modelMapper;
    }

    public void createPost(CreatePost createPost){
        Users user = usersRepo.findById(createPost.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author of the post does not exist"));
        Post post = modelMapper.map(createPost, Post.class);
        post.setAuthor(user);
        postRepo.save(post);
    }

    public List<PostResponse> getAllPost(Pageable pageable){
        return postRepo.findAll(pageable).stream().map(val -> modelMapper.map(val, PostResponse.class)).toList();
    }

    @Transactional
    public void addLike(long postId, long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        user.addLike(post);
        post.addLike(user);
        post.setLikeCount(post.getLikeCount()+1);
    }

    @Transactional
    public void removeLike(long postId, long userId){
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        user.getPostLikes().remove(post);
        post.getLikes().remove(user);
        post.setLikeCount(post.getLikeCount()-1);
    }
}
