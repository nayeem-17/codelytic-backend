package com.example.codelytic.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.codelytic.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
