package com.example.codelytic.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/discussion")
public class PostController {
    @Autowired
    private PostService discussionService;
    /*
     * 
     * List of routes
     * - create Post
     * - update Post
     * - delete Post
     * - get one Post
     * - get all Post
     */

}
