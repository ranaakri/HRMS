package com.mycompany.hrms.data.repository.post;

import com.mycompany.hrms.data.entity.post.PostComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentsRepo extends JpaRepository<PostComments, Long> {

    Page<PostComments> findAllByPost_PostIdAndIsDeletedFalse(Long postId, Pageable pageable);
}
