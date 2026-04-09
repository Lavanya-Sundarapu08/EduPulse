package com.edupulse.controller;

import com.edupulse.service.PrincipalDashboardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("/principal")
public class PrincipalController {

    @Autowired private PrincipalDashboardService dashboardService;
    @Autowired private ObjectMapper objectMapper;

    @GetMapping("/dashboard")
    public String dashboard(Model model) throws Exception {

        Map<String, Object> compData = dashboardService.getFacultyComparisonData();

        System.out.println("=== PRINCIPAL DASHBOARD DEBUG ===");
        System.out.println("compData.get('names'): " + compData.get("names"));
        System.out.println("compData.get('teaching'): " + compData.get("teaching"));
        System.out.println("compData.get('counts'): " + compData.get("counts"));

        model.addAttribute("totalSubmissions", dashboardService.getTotalSubmissions());
        model.addAttribute("facultyCount", ((java.util.List<?>) compData.get("names")).size());

        String namesJson = objectMapper.writeValueAsString(compData.get("names"));
        String teachingJson = objectMapper.writeValueAsString(compData.get("teaching"));
        String subjectJson = objectMapper.writeValueAsString(compData.get("subject"));
        String commJson = objectMapper.writeValueAsString(compData.get("comm"));
        String punctJson = objectMapper.writeValueAsString(compData.get("punct"));
        String supportJson = objectMapper.writeValueAsString(compData.get("support"));
        String countsJson = objectMapper.writeValueAsString(compData.get("counts"));
        String ratingJson = objectMapper.writeValueAsString(dashboardService.getOverallRatingDistribution());
        String branchJson = objectMapper.writeValueAsString(dashboardService.getBranchWiseCounts());

        System.out.println("namesJson: " + namesJson);
        System.out.println("teachingJson: " + teachingJson);
        System.out.println("ratingJson: " + ratingJson);
        System.out.println("branchJson: " + branchJson);

        model.addAttribute("facultyNamesJson", namesJson);
        model.addAttribute("teachingJson", teachingJson);
        model.addAttribute("subjectJson", subjectJson);
        model.addAttribute("commJson", commJson);
        model.addAttribute("punctJson", punctJson);
        model.addAttribute("supportJson", supportJson);
        model.addAttribute("countsJson", countsJson);
        model.addAttribute("ratingDistJson", ratingJson);
        model.addAttribute("branchCountsJson", branchJson);

        return "principal/dashboard";
    }
}