package com.example.codelytic.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.codelytic.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT COUNT(*) FROM comment WHERE commented_by = ?1", nativeQuery = true)
    Long countByCommentedBy(String commentedBy);

}
