package com.edupulse.service;

import com.edupulse.model.Feedback;
import com.edupulse.model.FacultyConfig;
import com.edupulse.repository.FacultyConfigRepository;
import com.edupulse.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired private FeedbackRepository feedbackRepo;
    @Autowired private FacultyConfigRepository facultyConfigRepo;

    public List<FacultyConfig> getFacultyList(String semester, String branch) {
        return facultyConfigRepo.findBySemesterAndBranch(semester, branch);
    }

    public boolean alreadySubmitted(String semester, String branch, String section, String facultyName) {
        return feedbackRepo.existsBySemesterAndBranchAndSectionAndFacultyName(
            semester, branch, section, facultyName);
    }

    public void saveFeedback(Feedback feedback) {
        feedbackRepo.save(feedback);
    }

    public void saveAllFeedback(List<Feedback> feedbackList) {
        feedbackRepo.saveAll(feedbackList);
    }
}