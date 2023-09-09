package com.example.codelytic.progress;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.codelytic.progress.model.DailyProgress;
import com.example.codelytic.progress.model.Progress;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

}

interface DailyProgressRepository extends JpaRepository<DailyProgress, Long> {

}