package com.example.codelytic.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/like")
@RestController
public class LikeController {
    @Autowired

    private LikeService likeService;

    @PostMapping("/{commentId}")
    public ResponseEntity<?> like(Long commentId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String responseMessage = this.likeService.like(email, commentId);
        return ResponseEntity.ok(responseMessage);
    }
}
