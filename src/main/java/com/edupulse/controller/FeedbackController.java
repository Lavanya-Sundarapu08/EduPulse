package com.edupulse.controller;

import com.edupulse.model.Feedback;
import com.edupulse.model.FacultyConfig;
import com.edupulse.service.FeedbackService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // ─── Semester options ──────────────────────────────────────────────────────
    private static final List<String> SEMESTERS = List.of(
        "Semester I","Semester II","Semester III","Semester IV",
        "Semester V","Semester VI","Semester VII","Semester VIII"
    );

    private static final List<String> BRANCHES = List.of(
        "Computer Science (CSE)", "Electronics (ECE)",
        "Mechanical (ME)", "Civil (CE)", "IT"
    );

    private static final List<String> SECTIONS = List.of(
        "Section A", "Section B", "Section C"
    );

    // ─── Home page (student details form) ─────────────────────────────────────
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("semesters", SEMESTERS);
        model.addAttribute("branches",  BRANCHES);
        model.addAttribute("sections",  SECTIONS);
        return "feedback/home";
    }

    // ─── Begin feedback: load faculty list and store context in session ────────
    @PostMapping("/feedback/begin")
    public String beginFeedback(
            @RequestParam String semester,
            @RequestParam String branch,
            @RequestParam String section,
            HttpSession session,
            Model model) {

        List<FacultyConfig> facultyList = feedbackService.getFacultyList(semester, branch);

        if (facultyList.isEmpty()) {
            model.addAttribute("selectedSemester", semester);
            model.addAttribute("selectedBranch",   branch);
            model.addAttribute("selectedSection",  section);
            model.addAttribute("error", "No faculty configured for " + semester + " / " + branch + ". Please contact admin.");
            model.addAttribute("semesters", SEMESTERS);
            model.addAttribute("branches",  BRANCHES);
            model.addAttribute("sections",  SECTIONS);
            return "feedback/home";
        }

        // Store in session so we can navigate next/prev
        session.setAttribute("semester",    semester);
        session.setAttribute("branch",      branch);
        session.setAttribute("section",     section);
        session.setAttribute("facultyList", facultyList);
        session.setAttribute("currentIndex", 0);
        session.setAttribute("feedbackMap", new HashMap<String, Feedback>());

        return "redirect:/feedback/faculty/0";
    }

    // ─── Show feedback form for faculty at index ───────────────────────────────
    @GetMapping("/feedback/faculty/{index}")
    public String showFacultyFeedback(@PathVariable int index, HttpSession session, Model model) {
        List<FacultyConfig> facultyList = (List<FacultyConfig>) session.getAttribute("facultyList");
        if (facultyList == null) return "redirect:/";

        if (index >= facultyList.size()) return "redirect:/feedback/review";

        FacultyConfig faculty = facultyList.get(index);
        session.setAttribute("currentIndex", index);

        // Pre-fill if they already filled this one
        Map<String, Feedback> feedbackMap = (Map<String, Feedback>) session.getAttribute("feedbackMap");
        Feedback existing = feedbackMap.get(faculty.getFacultyName());

        model.addAttribute("faculty",      faculty);
        model.addAttribute("index",        index);
        model.addAttribute("total",        facultyList.size());
        model.addAttribute("semester",     session.getAttribute("semester"));
        model.addAttribute("branch",       session.getAttribute("branch"));
        model.addAttribute("section",      session.getAttribute("section"));
        model.addAttribute("feedback",     existing != null ? existing : new Feedback());
        model.addAttribute("facultyList",  facultyList);

        return "feedback/faculty-form";
    }

    // ─── Save one faculty's feedback and move to next ─────────────────────────
    @PostMapping("/feedback/faculty/{index}/save")
    public String saveFacultyFeedback(
            @PathVariable int index,
            @ModelAttribute Feedback feedback,
            @RequestParam(required = false) String action,
            HttpSession session) {

        List<FacultyConfig> facultyList = (List<FacultyConfig>) session.getAttribute("facultyList");
        Map<String, Feedback> feedbackMap = (Map<String, Feedback>) session.getAttribute("feedbackMap");

        FacultyConfig faculty = facultyList.get(index);

        // Populate context fields
        feedback.setSemester((String) session.getAttribute("semester"));
        feedback.setBranch((String) session.getAttribute("branch"));
        feedback.setSection((String) session.getAttribute("section"));
        feedback.setFacultyName(faculty.getFacultyName());
        feedback.setFacultyFullName(faculty.getFacultyFullName());
        feedback.setSubject(faculty.getSubject());
        feedback.setSubjectType(faculty.getSubjectType());

        feedbackMap.put(faculty.getFacultyName(), feedback);
        session.setAttribute("feedbackMap", feedbackMap);

        if ("prev".equals(action)) {
            return "redirect:/feedback/faculty/" + (index - 1);
        }

        int next = index + 1;
        if (next >= facultyList.size()) {
            return "redirect:/feedback/review";
        }
        return "redirect:/feedback/faculty/" + next;
    }

    // ─── Review page (shows all faculty + overall dept feedback) ──────────────
    @GetMapping("/feedback/review")
    public String reviewPage(HttpSession session, Model model) {
        List<FacultyConfig> facultyList = (List<FacultyConfig>) session.getAttribute("facultyList");
        if (facultyList == null) return "redirect:/";

        Map<String, Feedback> feedbackMap = (Map<String, Feedback>) session.getAttribute("feedbackMap");

        model.addAttribute("facultyList",  facultyList);
        model.addAttribute("feedbackMap",  feedbackMap);
        model.addAttribute("semester",     session.getAttribute("semester"));
        model.addAttribute("branch",       session.getAttribute("branch"));
        model.addAttribute("section",      session.getAttribute("section"));

        return "feedback/review";
    }

    // ─── Submit all feedback ───────────────────────────────────────────────────
    @PostMapping("/feedback/submit")
    public String submitAll(
            @RequestParam(required = false) String overallDeptFeedback,
            @RequestParam(required = false) String overallRating,
            HttpSession session) {

        Map<String, Feedback> feedbackMap = (Map<String, Feedback>) session.getAttribute("feedbackMap");
        List<FacultyConfig> facultyList = (List<FacultyConfig>) session.getAttribute("facultyList");

        if (feedbackMap == null || feedbackMap.isEmpty()) return "redirect:/";

        // Set overall dept feedback on ALL feedback entries (last faculty gets it; or store separately)
        List<Feedback> toSave = new ArrayList<>(feedbackMap.values());
        for (int i = 0; i < toSave.size(); i++) {
            if (i == toSave.size() - 1) {
                // Store overall info only on last entry for simplicity
                toSave.get(i).setOverallDeptFeedback(overallDeptFeedback);
                toSave.get(i).setOverallRating(overallRating);
            }
        }

        feedbackService.saveAllFeedback(toSave);

        // Clear session
        session.removeAttribute("feedbackMap");
        session.removeAttribute("facultyList");
        session.removeAttribute("semester");
        session.removeAttribute("branch");
        session.removeAttribute("section");
        session.removeAttribute("currentIndex");

        return "redirect:/feedback/success";
    }

    // ─── Success page ──────────────────────────────────────────────────────────
    @GetMapping("/feedback/success")
    public String successPage() {
        return "feedback/success";
    }

    // ─── API: get faculty list by semester+branch (for dynamic dropdowns) ──────
    @GetMapping("/api/faculty")
    @ResponseBody
    public List<FacultyConfig> getFacultyApi(
            @RequestParam String semester,
            @RequestParam String branch) {
        return feedbackService.getFacultyList(semester, branch);
    }
}