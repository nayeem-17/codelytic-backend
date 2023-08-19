package com.example.codelytic.tag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/tags")

public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    List<Tag> getTags(
       
    ) {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        return tagService.getTags();
    }

    @PostMapping
    ResponseEntity<Object> createTags(
            @RequestBody List<String> tagNames) {
        if (tagNames == null) {
            throw new IllegalArgumentException(
                    "The request body is empty or does not contain the required data for the CreateTagDTO object.");
        }
        System.out.println(tagNames);
        List<Tag> tags = new ArrayList<>(tagNames.size());
        tagNames.stream().forEach(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);
            tags.add(tag);
        });
        tagService.createTags(tags);
        return ResponseEntity.ok().build();
    }
}
