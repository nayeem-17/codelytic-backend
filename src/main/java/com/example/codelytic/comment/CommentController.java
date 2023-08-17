package com.example.codelytic.comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codelytic.comment.model.Comment;
import com.example.codelytic.comment.model.CreateCommentDTO;
import com.example.codelytic.post.PostService;
import com.example.codelytic.post.model.Post;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    /*
     * ROUTE LIST
     * 
     * post comment
     * reply to a comment which is also a post comment
     * get all comments of a post
     * get all replies of a comment
     * update a comment
     * delete a comment, which will trigger to delete all it's children
     * comments(replies)
     * 
     */
    /*
     * post comment
     * reply to a comment which is also a post comment
     */
    @PostMapping
    ResponseEntity<?> createComment(@RequestBody CreateCommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());

        if (commentDTO.getPostId() == null || commentDTO.getPostId() < 1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid post id");
            return ResponseEntity.badRequest().body(response);
        }

        // Fetch the post
        Post post = postService.getPost(commentDTO.getPostId());

        if (post == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "The post does not exist.");
            return ResponseEntity.badRequest().body(response);
        }
        comment.setPost(post);
        // post.getComments().add(comment);

        if (commentDTO.getParentCommentId() != null && commentDTO.getParentCommentId() > 0) {
            Comment parentComment = commentService.findById(commentDTO.getParentCommentId());

            if (parentComment == null) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "The parent comment does not exist.");
                return ResponseEntity.badRequest().body(response);
            }

            comment.setParentComment(parentComment);
        } else
            post.getComments().add(comment); // Add comment to the post's comments list
        System.out.println(
                post.getComments().size() + " is the size bitch");
        return commentService.createComment(comment);
    }

    /*
     * Get all post by a post Id
     */
    @GetMapping("/{postId}")
    List<Comment> getCommentsByPostId(@PathVariable Long postId) {
        return postService.getPost(postId).getComments();
    }

    /*
     * update a comment content
     */
    @PutMapping("/{commentId}")
    ResponseEntity<?> updateComment(
            @PathVariable Long commentId, @RequestBody String content) {
        Comment comment = this.commentService.findById(commentId);
        comment.setContent(content);
        return this.commentService.updateComment(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        this.postService.deleteCommentAndChildren(commentId);
        return ResponseEntity.ok().build();
    }

}
