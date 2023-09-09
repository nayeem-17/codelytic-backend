package com.example.codelytic.like;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "SELECT * FROM _like WHERE comment_id = ?1 AND liked_by = ?2", nativeQuery = true)
    Optional<Like> findByCommentAndLikedBy(Long commentId, String email);

}
