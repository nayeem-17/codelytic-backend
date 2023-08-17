package com.example.codelytic.post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.codelytic.post.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
