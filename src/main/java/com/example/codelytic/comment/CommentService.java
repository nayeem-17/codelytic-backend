package com.example.codelytic.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.codelytic.comment.model.Comment;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public ResponseEntity<?> createComment(Comment comment) {
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    public Comment findById(Long parentCommentId) {
        return commentRepository.findById(parentCommentId).orElse(null);
    }

    public ResponseEntity<?> updateComment(Comment updatedComment) {
        Comment comment = this.commentRepository.findById(updatedComment.getId()).orElse(null);
        if (comment == null) {
            return ResponseEntity.badRequest().body("Comment not found");
        }
        comment.setContent(updatedComment.getContent());
        return ResponseEntity.ok(this.commentRepository.save(comment));
    }
    // delete a comment with all it's child comment

    public Long getNumberOfCommentsByUser(String commentedBy) {
        return this.commentRepository
                .countByCommentedBy(commentedBy);
    }

    // public void deleteCommentAndChildren(Long commentId) {
    // Comment comment = this.commentRepository.findById(commentId).orElse(null);
    // if (comment != null)
    // this.commentRepository.delete(comment);
    // }
}
