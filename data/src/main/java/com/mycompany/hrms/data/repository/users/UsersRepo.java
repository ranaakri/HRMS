package com.mycompany.hrms.data.repository.users;

import com.mycompany.hrms.data.entity.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<Users> findUsersByAssignedUnder_UserId(long assignedUnderUserId);

    Page<Users> findAllByDepartment_DepartmentId(Long departmentId, Pageable pageable);

    List<Users> findUsersByUserIdIn(List<Long> userId);

    List<Users> findByNameStartingWith(String name);

    List<Users> findAllByDepartment_DepartmentIdAndNameStartingWith(Long departmentId, String name);

    @Query(value = "SELECT * FROM Users " +
            "WHERE DAY(SWITCHOFFSET(birthdate, '+05:30')) = DAY(SWITCHOFFSET(SYSDATETIMEOFFSET(), '+05:30')) \n" +
            "  AND MONTH(SWITCHOFFSET(birthdate, '+05:30')) = MONTH(SWITCHOFFSET(SYSDATETIMEOFFSET(), '+05:30'))", nativeQuery = true)
    List<Users> findUserWithBirthday();

    @Query(value = """
SELECT * FROM Users
            WHERE DAY(SWITCHOFFSET(joiningDate, '+05:30')) = DAY(SWITCHOFFSET(SYSDATETIMEOFFSET(), '+05:30'))
            AND MONTH(SWITCHOFFSET(joiningDate, '+05:30')) = MONTH(SWITCHOFFSET(SYSDATETIMEOFFSET(), '+05:30'))""", nativeQuery = true)
    List<Users> findUserWithJoiningAnniversary();

    boolean existsByDepartment_DepartmentId(Long departmentId);

    @Query(value = "EXEC dbo.CheckUserAssignment @UserId = :userId, @AssignedUnderId = :assignedUnder", nativeQuery = true)
    boolean checkUserAssign(@Param("userId") long userId, @Param("assignedUnder") long assignedUnder);
}