package com.edupulse.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Student Details
    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String section;

    // Faculty Details
    @Column(name = "faculty_name", nullable = false)
    private String facultyName;

    @Column(name = "faculty_full_name")
    private String facultyFullName;

    @Column(name = "subject")
    private String subject;

    @Column(name = "subject_type")
    private String subjectType;

    // Ratings
    @Column(name = "teaching_effectiveness")
    private Integer teachingEffectiveness;

    @Column(name = "subject_knowledge")
    private Integer subjectKnowledge;

    @Column(name = "communication_skills")
    private Integer communicationSkills;

    @Column(name = "punctuality")
    private Integer punctuality;

    @Column(name = "student_support")
    private Integer studentSupport;

    // Comments
    @Column(name = "personal_comment", columnDefinition = "TEXT")
    private String personalComment;

    @Column(name = "overall_dept_feedback", columnDefinition = "TEXT")
    private String overallDeptFeedback;

    @Column(name = "overall_rating")
    private String overallRating;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public String getFacultyFullName() { return facultyFullName; }
    public void setFacultyFullName(String facultyFullName) { this.facultyFullName = facultyFullName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getSubjectType() { return subjectType; }
    public void setSubjectType(String subjectType) { this.subjectType = subjectType; }

    public Integer getTeachingEffectiveness() { return teachingEffectiveness; }
    public void setTeachingEffectiveness(Integer v) { this.teachingEffectiveness = v; }

    public Integer getSubjectKnowledge() { return subjectKnowledge; }
    public void setSubjectKnowledge(Integer v) { this.subjectKnowledge = v; }

    public Integer getCommunicationSkills() { return communicationSkills; }
    public void setCommunicationSkills(Integer v) { this.communicationSkills = v; }

    public Integer getPunctuality() { return punctuality; }
    public void setPunctuality(Integer v) { this.punctuality = v; }

    public Integer getStudentSupport() { return studentSupport; }
    public void setStudentSupport(Integer v) { this.studentSupport = v; }

    public String getPersonalComment() { return personalComment; }
    public void setPersonalComment(String personalComment) { this.personalComment = personalComment; }

    public String getOverallDeptFeedback() { return overallDeptFeedback; }
    public void setOverallDeptFeedback(String v) { this.overallDeptFeedback = v; }

    public String getOverallRating() { return overallRating; }
    public void setOverallRating(String overallRating) { this.overallRating = overallRating; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}