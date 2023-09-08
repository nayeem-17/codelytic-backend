package com.example.codelytic.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codelytic.comment.CommentRepository;
import com.example.codelytic.comment.model.Comment;
import com.example.codelytic.user.UserRepository;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    public String like(String email, Long commentId) {
        String responseMessage = "";
        // User likedBy = this.userRepository.findByEmail(email).orElseThrow(
        // () -> new IllegalArgumentException("user with id " + email + " not found"));
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("comment with id " + commentId + " not found"));
        Like existingLike = this.likeRepository.findByCommentAndLikedBy(commentId, email).orElse(null);
        /*
         * if the user has already liked the comment, then unlike it
         */
        if (existingLike != null) {

            this.likeRepository.delete(existingLike);
            responseMessage = "unliked";
        } else {

            Like like = new Like();
            like.setComment(comment);
            like.setLikedBy(email);
            this.likeRepository.save(like);
            responseMessage = "liked";
        }

        return responseMessage;
    }

}
