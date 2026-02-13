package com.mycompany.hrms.data.repository.users;

import com.mycompany.hrms.data.entity.user.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Long> {
    @EntityGraph(attributePaths = {"role"})
    Optional<Users> findUsersByEmail(String email);
    boolean existsByEmail(String email);
    List<Users> findUsersByNameLike(String name);

    @Query("FROM Users u WHERE DAY(u.birthdate) = DAY(CURRENT_DATE) AND MONTH(u.birthdate) = MONTH(CURRENT_DATE)")
    List<Users> findUsersWithBirthdayToday();
}