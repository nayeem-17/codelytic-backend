package com.example.codelytic.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codelytic.comment.CommentRepository;
import com.example.codelytic.comment.model.Comment;
import com.example.codelytic.post.model.Post;

@Service
public class PostService {
    @Autowired
    private PostRepository discussionRepository;
    @Autowired
    CommentRepository commentRepository;

    public Post createPost(Post post) {
        return discussionRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return discussionRepository.findAll();
    }

    public Post getPost(Long postId) {
        return discussionRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public boolean deletePost(Long postId) {
        discussionRepository.deleteById(postId);
        return true;
    }

    public Post updatePost(Post updatedPost) {
        // first find if the post exists or not, then update the post
        Post post = discussionRepository
                .findById(updatedPost.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setTags(updatedPost.getTags());
        return discussionRepository.save(post);
    }

    public void deleteCommentAndChildren(Long commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            // Remove the comment from the post's comments list
            Post post = comment.getPost();
            if (post != null) {
                post.getComments().remove(comment);
            }

            // Clear parent and post references for child comments
            for (Comment child : comment.getChildComments()) {
                child.setParentComment(null);
                child.setPost(null);
            }

            // Clear post reference for the comment
            comment.setPost(null);

            // Delete the comment from the post_comments table
            if (comment.getParentComment() != null) {
                comment.getParentComment().getChildComments().remove(comment);
            }

            this.commentRepository.delete(comment);
        }
    }

}
