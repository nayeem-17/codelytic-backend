package com.example.codelytic.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    public Tag findById(Long id) {
        return tagRepository
                .findById(id)
                .orElse(null);
    }

    public void createTags(List<Tag> tags) {
        tagRepository.saveAll(tags);
    }

}
