package com.example.codelytic.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;
}
