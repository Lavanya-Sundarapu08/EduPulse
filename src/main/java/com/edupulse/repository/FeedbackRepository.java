package com.edupulse.repository;

import com.edupulse.model.Feedback;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // ✅ Duplicate check
    boolean existsBySemesterAndBranchAndSectionAndFacultyName(
        String semester, String branch, String section, String facultyName);

    // =========================
    // ✅ STAFF METHODS
    // =========================

    @Query("SELECT f FROM Feedback f WHERE LOWER(f.facultyName) = LOWER(:name)")
    List<Feedback> findByFacultyName(@Param("name") String facultyName);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE LOWER(f.facultyName) = LOWER(:name)")
    long countByFacultyName(@Param("name") String facultyName);

    @Query("SELECT AVG(f.teachingEffectiveness), AVG(f.subjectKnowledge), " +
           "AVG(f.communicationSkills), AVG(f.punctuality), AVG(f.studentSupport) " +
           "FROM Feedback f WHERE LOWER(f.facultyName) = LOWER(:name)")
@SuppressWarnings("unchecked")
    List<Object[]> getAverageScores(@Param("name") String facultyName);

    @Query("SELECT f.overallRating, COUNT(f) FROM Feedback f " +
           "WHERE LOWER(f.facultyName) = LOWER(:name) GROUP BY f.overallRating")
    List<Object[]> getRatingDistribution(@Param("name") String facultyName);

    @Query("SELECT f.semester, AVG(f.teachingEffectiveness) FROM Feedback f " +
           "WHERE LOWER(f.facultyName) = LOWER(:name) GROUP BY f.semester ORDER BY f.semester")
    List<Object[]> getSemesterTrend(@Param("name") String facultyName);

    @Query("SELECT f.section, f.branch, AVG(f.teachingEffectiveness), " +
           "AVG(f.subjectKnowledge), AVG(f.communicationSkills), " +
           "AVG(f.punctuality), AVG(f.studentSupport) " +
           "FROM Feedback f WHERE LOWER(f.facultyName) = LOWER(:name) " +
           "GROUP BY f.section, f.branch")
    List<Object[]> getSectionWiseAverages(@Param("name") String facultyName);

    @Query("SELECT f FROM Feedback f WHERE LOWER(f.facultyName) = LOWER(:name) " +
           "AND f.personalComment IS NOT NULL AND f.personalComment <> '' " +
           "ORDER BY f.submittedAt DESC")
    List<Feedback> getRecentComments(@Param("name") String facultyName);

    // =========================
    // ✅ PRINCIPAL METHODS
    // =========================

    @Query("SELECT f.facultyName, " +
           "AVG(f.teachingEffectiveness), AVG(f.subjectKnowledge), " +
           "AVG(f.communicationSkills), AVG(f.punctuality), AVG(f.studentSupport), " +
           "COUNT(*) " +
           "FROM Feedback f GROUP BY f.facultyName")
    List<Object[]> getAllFacultyAverages();

    @Query("SELECT f.overallRating, COUNT(*) FROM Feedback f " +
           "WHERE f.overallRating IS NOT NULL " +
           "GROUP BY f.overallRating")
    List<Object[]> getOverallRatingDistribution();

    @Query("SELECT f.branch, COUNT(*) FROM Feedback f GROUP BY f.branch")
    List<Object[]> getBranchWiseCounts();
}