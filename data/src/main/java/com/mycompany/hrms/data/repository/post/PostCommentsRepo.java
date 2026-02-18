package com.mycompany.hrms.data.repository.post;

import com.mycompany.hrms.data.entity.post.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentsRepo extends JpaRepository<PostComments, Long> {
}
