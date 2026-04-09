package com.edupulse.model;

import jakarta.persistence.*;

@Entity
@Table(name = "faculty_config")
public class FacultyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "faculty_name", nullable = false)
    private String facultyName;         // short key e.g. "Kumar"

    @Column(name = "faculty_full_name", nullable = false)
    private String facultyFullName;     // e.g. "Dr. A. Kumar"

    @Column(nullable = false)
    private String subject;

    @Column(name = "subject_type")
    private String subjectType;         // THEORY / LAB

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private String semester;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String v) { this.facultyName = v; }

    public String getFacultyFullName() { return facultyFullName; }
    public void setFacultyFullName(String v) { this.facultyFullName = v; }

    public String getSubject() { return subject; }
    public void setSubject(String v) { this.subject = v; }

    public String getSubjectType() { return subjectType; }
    public void setSubjectType(String v) { this.subjectType = v; }

    public String getBranch() { return branch; }
    public void setBranch(String v) { this.branch = v; }

    public String getSemester() { return semester; }
    public void setSemester(String v) { this.semester = v; }
}