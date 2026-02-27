package com.mycompany.hrms.data.repository.users;

import com.mycompany.hrms.data.entity.user.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

    List<Users> findUsersByUserIdIn(List<Long> userId);

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

//    @Query(value = "WITH UppersCTE AS ( SELECT   userId, profileUrl, designation, assignedUnder, 0 AS level FROM users WHERE userId = :userId" +
//            "    UNION ALL SELECT u.userId, u.profileUrl, u.designation, u.assignedUnder, cte.level + 1 FROM users u" +
//            "    INNER JOIN UppersCTE cte ON u.userId = cte.assignedUnder" +
//            ")" +
//            "SELECT *" +
//            "FROM UppersCTE " +
//            "WHERE userId <> :userId" +
//            " ORDER BY level desc OPTION (MAXRECURSION 10);", nativeQuery = true)
//    List<OrgChartRes> getOrgChart(@Param("userId") long userId);
}