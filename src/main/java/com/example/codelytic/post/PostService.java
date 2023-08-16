package com.example.codelytic.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository discussionRepository;

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
}
