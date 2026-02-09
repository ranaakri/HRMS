package com.mycompany.hrms.data.repository.users;

import com.mycompany.hrms.data.entity.user.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Long> {
    @EntityGraph(attributePaths = {"role"})
    Optional<Users> findUsersByEmail(String email);
    boolean existsByEmail(String email);
}