package com.mycompany.hrms.data.repository.post;

import com.mycompany.hrms.data.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
}
