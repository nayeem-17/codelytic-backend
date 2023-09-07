package com.example.codelytic.progress.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.codelytic.course.model.schema.Course;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = { "createdAt", "updatedAt", "course" })
// @Table(uniqueConstraints = @UniqueConstraint(columnNames = { "course_id",
// "id" }))
public class CourseProgress {
    public CourseProgress(Course course) {
        this.course = course;
        int subsectionLength = course.getSubsections().size();
        this.subsectionsProgresses = new ArrayList<>(subsectionLength);
        for (int i = 0; i < subsectionLength; i++) {
            this.subsectionsProgresses.add(new SubsectionProgress(course.getSubsections().get(i)));
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SubsectionProgress> subsectionsProgresses;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public float getCourseProgressInPercentage() {
        int subsectionLength = this.subsectionsProgresses.size();
        float totalProgress = 0.0f;
        for (int i = 0; i < subsectionLength; i++) {
            System.out.println(
                    "Subsection " + this.subsectionsProgresses.get(i).getName() + " progress: "
                            + this.subsectionsProgresses.get(i).getSubsectionProgressInPercentage());
            totalProgress += this.subsectionsProgresses.get(i).getSubsectionProgressInPercentage();
        }
        if (subsectionLength > 0)
            return totalProgress / subsectionLength;
        return 0.0f;
    }
}
