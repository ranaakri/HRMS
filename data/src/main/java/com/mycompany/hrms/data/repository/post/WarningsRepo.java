package com.mycompany.hrms.data.repository.post;

import com.mycompany.hrms.data.entity.post.Warnings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarningsRepo extends JpaRepository<Warnings, Long> {
}
