package com.edupulse.service;

import com.edupulse.model.Feedback;
import com.edupulse.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StaffDashboardService {

    @Autowired
    private FeedbackRepository feedbackRepo;

    public Map<String, Double> getAverageScores(String facultyName) {
        List<Object[]> rows = feedbackRepo.getAverageScores(facultyName);
        Map<String, Double> result = new LinkedHashMap<>();

        if (rows == null || rows.isEmpty()) return result;

        // Spring JPA wraps single-row aggregate result as List<Object[]>
        Object[] row = rows.get(0);

        result.put("Teaching Effectiveness", row.length > 0 ? toDouble(row[0]) : 0);
        result.put("Subject Knowledge",      row.length > 1 ? toDouble(row[1]) : 0);
        result.put("Communication Skills",   row.length > 2 ? toDouble(row[2]) : 0);
        result.put("Punctuality",            row.length > 3 ? toDouble(row[3]) : 0);
        result.put("Student Support",        row.length > 4 ? toDouble(row[4]) : 0);

        return result;
    }

    public double getOverallAverage(String facultyName) {
        Map<String, Double> scores = getAverageScores(facultyName);
        if (scores.isEmpty()) return 0;
        return scores.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public Map<String, Long> getRatingDistribution(String facultyName) {
        List<Object[]> rows = feedbackRepo.getRatingDistribution(facultyName);
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("Excellent", 0L);
        result.put("Good", 0L);
        result.put("Average", 0L);
        result.put("Needs Improvement", 0L);
        for (Object[] r : rows) {
            String key = (String) r[0];
            if (key != null) result.put(key, ((Number) r[1]).longValue());
        }
        return result;
    }

    public Map<String, Double> getSemesterTrend(String facultyName) {
        List<Object[]> rows = feedbackRepo.getSemesterTrend(facultyName);
        Map<String, Double> result = new LinkedHashMap<>();
        for (Object[] r : rows) result.put((String) r[0], toDouble(r[1]));
        return result;
    }

    public List<Map<String, Object>> getSectionBreakdown(String facultyName) {
        List<Object[]> rows = feedbackRepo.getSectionWiseAverages(facultyName);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] r : rows) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("section",  r[0]);
            map.put("branch",   r[1]);
            map.put("teaching", toDouble(r[2]));
            map.put("subject",  toDouble(r[3]));
            map.put("comm",     toDouble(r[4]));
            map.put("punct",    toDouble(r[5]));
            map.put("support",  toDouble(r[6]));
            result.add(map);
        }
        return result;
    }

    public long getTotalSubmissions(String facultyName) {
        return feedbackRepo.countByFacultyName(facultyName);
    }

    public List<Feedback> getRecentComments(String facultyName) {
        List<Feedback> all = feedbackRepo.getRecentComments(facultyName);
        return all.size() > 5 ? all.subList(0, 5) : all;
    }

    private double toDouble(Object o) {
        if (o == null) return 0;
        if (o instanceof Number n) return Math.round(n.doubleValue() * 100.0) / 100.0;
        try { return Double.parseDouble(o.toString()); }
        catch (Exception e) { return 0; }
    }
}