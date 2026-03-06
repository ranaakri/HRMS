package com.mycompany.hrms.data.repository.post;

import com.mycompany.hrms.data.dtos.post.response.LikePostCount;
import com.mycompany.hrms.data.entity.post.Post;
import com.mycompany.hrms.data.entity.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Optional;

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

    @EntityGraph(attributePaths = {"mentions"})
    Optional<Post> findById(long postId);

    @EntityGraph(attributePaths = {"mentions"})
    Page<Post> findAllByIsDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"mentions"})
    Page<Post> findAllByMentionsContainsAndIsDeletedFalse(Users user, Pageable pageable);

    @EntityGraph(attributePaths = {"mentions"})
    Page<Post> findAllByAuthor_UserIdAndIsDeletedFalse(long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"mentions"})
    Page<Post> findAllByTagsContainingAndIsDeletedFalse(String tag, Pageable pageable);

    @EntityGraph(attributePaths = {"mentions"})
    @Query(value = "select * from post where createdAt between :startDate and :endDate and isDeleted = 0", nativeQuery = true)
    Page<Post> findAllBetweenStartDateAndEndDate(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate, Pageable pageable);

    @Query(value = "select COALESCE(SUM(p.likeCount), 0) as likeCount, COUNT(*) as postCount from Post p where authorId = :userId AND p.isDeleted = 0", nativeQuery = true)
    LikePostCount findLikeAndPostCountByUserId(@Param("userId") Long userId);

    @Query(value = "select COUNT(*) from PostMentions pm join Post p on p.postId = pm.postId where pm.userId = :userId AND p.isDeleted = 0", nativeQuery = true)
    long findMentionCountByUserId(@Param("userId") long userId);

    @Query(value = """
        SELECT p.*
            FROM Post p
            LEFT JOIN Warnings w
                ON p.postId = w.entityId
                AND w.entityType = 'POST'
            WHERE p.isDeleted = 1
              AND p.authorId = :userId
              AND w.entityId IS NULL
            """, nativeQuery = true)
    Page<Post> findAllDeletedPostByUser(@Param("userId") Long userId, Pageable pageable);
}
