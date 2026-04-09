package com.edupulse.controller;

import com.edupulse.model.User;
import com.edupulse.repository.UserRepository;
import com.edupulse.service.StaffDashboardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired private StaffDashboardService dashboardService;
    @Autowired private UserRepository userRepo;
    @Autowired private ObjectMapper objectMapper;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) throws Exception {

        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();

        // ✅ FIX HERE
        String facultyName = user.getFacultyName();
        facultyName = facultyName.substring(0,1).toUpperCase() + facultyName.substring(1).toLowerCase();

        model.addAttribute("staffName",   user.getFullName());
        model.addAttribute("facultyName", facultyName);

        model.addAttribute("totalSubmissions", dashboardService.getTotalSubmissions(facultyName));
        model.addAttribute("overallAverage",   dashboardService.getOverallAverage(facultyName));
        model.addAttribute("recentComments",   dashboardService.getRecentComments(facultyName));

        model.addAttribute("avgScoresJson", objectMapper.writeValueAsString(
                dashboardService.getAverageScores(facultyName)));

        model.addAttribute("ratingDistJson", objectMapper.writeValueAsString(
                dashboardService.getRatingDistribution(facultyName)));

        model.addAttribute("semesterTrendJson", objectMapper.writeValueAsString(
                dashboardService.getSemesterTrend(facultyName)));

        model.addAttribute("sectionBreakdownJson", objectMapper.writeValueAsString(
                dashboardService.getSectionBreakdown(facultyName)));

        return "staff/dashboard";
    }
}