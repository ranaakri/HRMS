package com.mycompany.hrms.data.repository.post;

import com.mycompany.hrms.data.entity.post.Post;
import com.mycompany.hrms.data.entity.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

public interface PostRepo extends JpaRepository<Post, Long> {

    @Query(value = "SELECT CASE WHEN EXISTS ( " +
            "    SELECT 1 FROM PostLikes " +
            "    WHERE userId = :userId AND postId = :postId" +
            ") THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END",
            nativeQuery = true)
    boolean likeExistsOnPostByUserIdAndPostId(@Param("userId") long userId, @Param("postId") long postId);

    @Query("""
    SELECT u FROM Post p
    JOIN p.likes u
    WHERE p.postId = :postId
""")
    Page<Users> findPostLikes(@Param("postId") Long postId, Pageable pageable);

    Page<Post> findAllByIsDeletedFalse(Pageable pageable);

    Page<Post> findAllByAuthor_UserIdAndIsDeletedFalse(long userId, Pageable pageable);

    Page<Post> findAllByTagsContainingAndIsDeletedFalse(String tag, Pageable pageable);

    @Query(value = "select * from post where createdAt between :startDate and :endDate and isDeleted = 0", nativeQuery = true)
    Page<Post> findAllBetweenStartDateAndEndDate(@Param("startDate")ZonedDateTime startDate, @Param("endDate")ZonedDateTime endDate, Pageable pageable);
}
