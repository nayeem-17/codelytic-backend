package com.example.codelytic.progress;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.codelytic.progress.model.Progress;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

}
