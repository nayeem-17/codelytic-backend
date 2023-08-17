package com.example.codelytic.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codelytic.subsection.Subsection;
import com.example.codelytic.subsection.SubsectionRepository;

@Service
public class LectureService {
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private SubsectionRepository subsectionRepository;

    public Lecture createLecture(Long subsectionId, Lecture lecture) {
        Subsection subsection = this.subsectionRepository.findById(subsectionId).orElse(null);
        if (subsection == null) {
            throw new RuntimeException("Subsection not found");
        }
        int lectureSize = subsection.getLectures().size();
        subsection.getLectures().add(lecture);
        return this.subsectionRepository.save(subsection).getLectures().get(lectureSize);
    }

    public void updateLecture(Long lectureId, Lecture updatedLecture) {
        Lecture lecture = this.lectureRepository.findById(lectureId).orElseThrow(
                () -> new RuntimeException("Lecture not found"));

        lecture.setTitle(updatedLecture.getTitle());
        lecture.setBody(updatedLecture.getBody());
        lecture.setLive(updatedLecture.isLive());

        this.lectureRepository.save(lecture);

    }

    public Lecture getLecture(Long lectureId) {
        return this.lectureRepository.findById(lectureId).orElseThrow(
                () -> new RuntimeException("Lecture not found"));
    }

    public boolean deleteLecture(
            Long lectureId) {
        this.lectureRepository.deleteById(lectureId);
        return true;
    }

}
