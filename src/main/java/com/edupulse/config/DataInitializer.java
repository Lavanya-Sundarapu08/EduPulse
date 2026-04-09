package com.edupulse.config;

import com.edupulse.model.FacultyConfig;
import com.edupulse.model.User;
import com.edupulse.model.Feedback;
import com.edupulse.repository.FacultyConfigRepository;
import com.edupulse.repository.UserRepository;
import com.edupulse.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepo;
    @Autowired private FacultyConfigRepository facultyConfigRepo;
    @Autowired private FeedbackRepository feedbackRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedFacultyConfig();
        seedFeedback();
    }

    private void seedUsers() {
        if (userRepo.count() > 0) return; // already seeded

        // ── Staff accounts (one per faculty) ─────────────────────
        createStaff("staff.kumar",   "password123", "Dr. A. Kumar",    "Kumar");
        createStaff("staff.rao",     "password123", "Prof. B. Rao",    "Rao");
        createStaff("staff.patel",   "password123", "Dr. C. Patel",    "Patel");
        createStaff("staff.sharma",  "password123", "Prof. D. Sharma", "Sharma");

        // ── Principal account ──────────────────────────────────────
        User principal = new User();
        principal.setUsername("principal");
        principal.setPassword(passwordEncoder.encode("admin123"));
        principal.setRole(User.Role.PRINCIPAL);
        principal.setFullName("Principal");
        userRepo.save(principal);

        System.out.println("✅ Default users seeded.");
        System.out.println("   Staff login   → username: staff.kumar  / password: password123");
        System.out.println("   Principal login → username: principal  / password: admin123");
    }

    private void createStaff(String username, String password, String fullName, String facultyName) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole(User.Role.STAFF);
        u.setFullName(fullName);
        u.setFacultyName(facultyName);
        userRepo.save(u);
    }

    private void seedFacultyConfig() {
        if (facultyConfigRepo.count() > 0) return;

        // ── Semester II, CSE branch ───────────────────────────────
        createFaculty("Kumar",  "Dr. A. Kumar",    "Operating Systems",        "THEORY", "Computer Science (CSE)", "Semester II");
        createFaculty("Rao",    "Prof. B. Rao",    "Data Structures",          "THEORY", "Computer Science (CSE)", "Semester II");
        createFaculty("Patel",  "Dr. C. Patel",    "Database Management",      "THEORY", "Computer Science (CSE)", "Semester II");
        createFaculty("Sharma", "Prof. D. Sharma", "Computer Networks",        "THEORY", "Computer Science (CSE)", "Semester II");

        // ── Semester II, ECE branch ───────────────────────────────
        createFaculty("Kumar",  "Dr. A. Kumar",    "Digital Electronics",      "THEORY", "Electronics (ECE)",      "Semester II");
        createFaculty("Rao",    "Prof. B. Rao",    "Signals & Systems",        "THEORY", "Electronics (ECE)",      "Semester II");

        System.out.println("✅ Faculty config seeded.");
    }

    private void createFaculty(String name, String fullName, String subject,
                                String type, String branch, String semester) {
        FacultyConfig fc = new FacultyConfig();
        fc.setFacultyName(name);
        fc.setFacultyFullName(fullName);
        fc.setSubject(subject);
        fc.setSubjectType(type);
        fc.setBranch(branch);
        fc.setSemester(semester);
        facultyConfigRepo.save(fc);
    }

    private void seedFeedback() {
        if (feedbackRepo.count() > 0) return; // already seeded

        // ── Sample feedback for Kumar ──────────────────────────────
        createFeedback("Kumar", "Dr. A. Kumar", "Operating Systems", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section A", 4, 4, 3, 4, 3, "Excellent");
        createFeedback("Kumar", "Dr. A. Kumar", "Operating Systems", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section B", 3, 3, 3, 3, 4, "Good");
        createFeedback("Kumar", "Dr. A. Kumar", "Operating Systems", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section A", 4, 4, 4, 4, 4, "Excellent");

        // ── Sample feedback for Rao ─────────────────────────────────
        createFeedback("Rao", "Prof. B. Rao", "Data Structures", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section A", 3, 4, 3, 3, 3, "Good");
        createFeedback("Rao", "Prof. B. Rao", "Data Structures", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section B", 3, 3, 2, 3, 2, "Average");
        createFeedback("Rao", "Prof. B. Rao", "Signals & Systems", "THEORY", 
                      "Semester II", "Electronics (ECE)", "Section A", 4, 4, 4, 4, 4, "Excellent");

        // ── Sample feedback for Patel ────────────────────────────────
        createFeedback("Patel", "Dr. C. Patel", "Database Management", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section A", 4, 4, 3, 4, 3, "Good");
        createFeedback("Patel", "Dr. C. Patel", "Database Management", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section B", 4, 4, 4, 4, 4, "Excellent");

        // ── Sample feedback for Sharma ──────────────────────────────
        createFeedback("Sharma", "Prof. D. Sharma", "Computer Networks", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section A", 3, 3, 3, 3, 3, "Average");
        createFeedback("Sharma", "Prof. D. Sharma", "Computer Networks", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section B", 2, 2, 2, 2, 2, "Average");
        createFeedback("Sharma", "Prof. D. Sharma", "Computer Networks", "THEORY", 
                      "Semester II", "Computer Science (CSE)", "Section A", 3, 3, 2, 3, 3, "Average");

        System.out.println("✅ Sample feedback data seeded.");
    }

    private void createFeedback(String facultyName, String facultyFullName, String subject,
                                 String subjectType, String semester, String branch, String section,
                                 int teaching, int subjectKnowledge, int comm, int punct, int support, String rating) {
        Feedback f = new Feedback();
        f.setFacultyName(facultyName);
        f.setFacultyFullName(facultyFullName);
        f.setSubject(subject);
        f.setSubjectType(subjectType);
        f.setSemester(semester);
        f.setBranch(branch);
        f.setSection(section);
        f.setTeachingEffectiveness(teaching);
        f.setSubjectKnowledge(subjectKnowledge);
        f.setCommunicationSkills(comm);
        f.setPunctuality(punct);
        f.setStudentSupport(support);
        f.setOverallRating(rating);
        feedbackRepo.save(f);
    }
}