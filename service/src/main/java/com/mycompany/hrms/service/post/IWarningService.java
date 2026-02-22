package com.mycompany.hrms.service.post;

import com.mycompany.hrms.service.dtos.post.response.PostResponse;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

public interface IWarningService {
    void deletePost(long postId, long warnedById,String reason, ZonedDateTime time);
    void deleteComment(long commentId, long warnedById,String reason, ZonedDateTime time);
    List<PostResponse> getWarnedPost(long userId);
}
