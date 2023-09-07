package com.example.codelytic.course.model.schema;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.codelytic.progress.model.CourseProgress;
import com.example.codelytic.subsection.Subsection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
@JsonIgnoreProperties(value = { "createdAt", "updatedAt", "courseProgress" })

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;
    private String title;
    private String icon;
    private boolean isPremium;
    private boolean isLive;
    @Column(length = 5120)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Subsection> subsections;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CourseProgress> courseProgress;

    private List<String> tags;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
