package com.example.codelytic.progress.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" })

public class SubsectionProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "lecture_progress", joinColumns = @JoinColumn(name = "subsection_progress_id"))
    @MapKeyColumn(name = "lecture_id")
    @Column(name = "completion_status")
    private Map<Long, Boolean> lectures = new HashMap<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private QuizProgress quizProgress;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}