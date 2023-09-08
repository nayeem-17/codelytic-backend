package com.example.codelytic.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codelytic.post.model.CreatePostDTO;
import com.example.codelytic.post.model.Post;
import com.example.codelytic.tag.TagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService discussionService;
    @Autowired
    private TagService tagService;

    /*
     * 
     * List of routes
     * - create Post
     * - update Post
     * - delete Post
     * - get one Post
     * - get all Post
     */
    @PostMapping
    /*
     * create post
     */
    ResponseEntity<?> createPost(
            @RequestBody CreatePostDTO postDTO) {

        Post post = new Post(postDTO);
        List<String> tags = new ArrayList<>();
        tags = postDTO
                .getTagIds()
                .stream()
                .map(tagId -> this.tagService.findById(tagId))
                .filter(Objects::nonNull)
                .map(tag -> tag.getName())
                .collect(Collectors.toList());
        // tags = tags_.stream()

        // System.out.println(
        // "tag_ids: " + Arrays.toString(postDTO.getTagIds().toArray()) + " " +
        // tags.size());
        // System.out.println(
        // "tags: " + Arrays.toString(tags.toArray()) + " " + tags.size());
        post.setTags(tags);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        post = discussionService.createPost(post, email);

        Map<String, Long> response = new HashMap<>();
        response.put("id", post.getId());

        return ResponseEntity.ok(response);
    }

    /*
     * update a post
     */
    @PutMapping("/{id}")
    ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody CreatePostDTO postDTO) {
        Post post = new Post(postDTO);
        List<String> tags = new ArrayList<>();

        tags = postDTO
                .getTagIds()
                .stream()
                .map(tagId -> this.tagService.findById(tagId).getName())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        post.setTags(tags);
        post.setId(id);
        post = discussionService.updatePost(post);

        Map<String, String> response = new HashMap<>();
        response.put("status", "updated");

        return ResponseEntity.ok().build();
    }

    /*
     * delete a post
     */
    @DeleteMapping("/{id}")
    ResponseEntity<?> deletePost(
            @PathVariable Long id) {
        discussionService.deletePost(id);

        Map<String, String> response = new HashMap<>();
        response.put("status", "deleted");

        return ResponseEntity.ok().build();
    }
    /*
     * get a single post
     */

    @GetMapping("/{id}")

    public Post getPost(
            @PathVariable Long id) {
        return discussionService.getPost(id);
    }

    /*
     * get all posts
     */
    @GetMapping
    public Iterable<Post> getAllPosts() {
        return discussionService.getAllPosts();
    }

}
