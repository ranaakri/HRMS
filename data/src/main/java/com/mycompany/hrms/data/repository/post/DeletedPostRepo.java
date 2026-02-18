package com.mycompany.hrms.data.repository.post;

import com.mycompany.hrms.data.entity.post.DeletedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedPostRepo extends JpaRepository<DeletedPost, Long> {
}
