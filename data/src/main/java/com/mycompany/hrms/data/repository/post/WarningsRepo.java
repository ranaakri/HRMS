package com.mycompany.hrms.data.repository.post;

import com.mycompany.hrms.data.entity.post.Warnings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarningsRepo extends JpaRepository<Warnings, Long> {
    List<Warnings> findAllByEntityTypeAndWarnedBy_UserId(Warnings.EntityType entityType, Long userId);
}
