package com.edupulse.repository;

import com.edupulse.model.FacultyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FacultyConfigRepository extends JpaRepository<FacultyConfig, Long> {
    List<FacultyConfig> findBySemesterAndBranch(String semester, String branch);
}