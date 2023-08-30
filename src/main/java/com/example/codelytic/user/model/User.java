package com.example.codelytic.user.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.codelytic.course.model.schema.Course;
import com.example.codelytic.like.Like;
import com.example.codelytic.post.model.Post;
import com.example.codelytic.progress.model.Progress;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * User
 */
@Data
@Entity
@Table(name = "_user")
@JsonIgnoreProperties(value = {
        "createdAt",
        "updatedAt",
        "posts",
        "likes"
})

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;

    private int subscriptionStatus = 0;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Post> posts;

    @JsonIgnoreProperties("likedBy")
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Like> likes;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "progress_id", referencedColumnName = "id")
    private Progress progress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> enrolledCourse;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}