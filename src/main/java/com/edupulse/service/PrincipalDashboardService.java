package com.edupulse.service;

import com.edupulse.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PrincipalDashboardService {

    @Autowired
    private FeedbackRepository feedbackRepo;

    /** Builds the complete faculty comparison dataset for charts */
    public Map<String, Object> getFacultyComparisonData() {
        List<Object[]> rows = feedbackRepo.getAllFacultyAverages();

        List<String> names   = new ArrayList<>();
        List<Double> teaching  = new ArrayList<>();
        List<Double> subject   = new ArrayList<>();
        List<Double> comm      = new ArrayList<>();
        List<Double> punct     = new ArrayList<>();
        List<Double> support   = new ArrayList<>();
        List<Long> counts      = new ArrayList<>();

        for (Object[] r : rows) {
            names.add((String) r[0]);
            teaching.add(toDouble(r[1]));
            subject.add(toDouble(r[2]));
            comm.add(toDouble(r[3]));
            punct.add(toDouble(r[4]));
            support.add(toDouble(r[5]));
            counts.add(((Number) r[6]).longValue());
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("names", names);
        data.put("teaching", teaching);
        data.put("subject", subject);
        data.put("comm", comm);
        data.put("punct", punct);
        data.put("support", support);
        data.put("counts", counts);

        return data;
    }

    /** Overall rating distribution across all faculty */
    public Map<String, Long> getOverallRatingDistribution() {
        List<Object[]> rows = feedbackRepo.getOverallRatingDistribution();
        Map<String, Long> result = new LinkedHashMap<>();

        result.put("Excellent", 0L);
        result.put("Good", 0L);
        result.put("Average", 0L);
        result.put("Needs Improvement", 0L);

        for (Object[] r : rows) {
        	String key = (r[0] == null || r[0].toString().isBlank())
        	        ? "No Rating"
        	        : r[0].toString();
            result.put(key, ((Number) r[1]).longValue());
        }

        return result;
    }

    /** Branch-wise submission counts */
    public Map<String, Long> getBranchWiseCounts() {
        List<Object[]> rows = feedbackRepo.getBranchWiseCounts();
        Map<String, Long> result = new LinkedHashMap<>();
        for (Object[] r : rows) {
            result.put((String) r[0], ((Number) r[1]).longValue());
        }
        return result;
    }

    /** Total submissions across all faculty */
    public long getTotalSubmissions() {
        return feedbackRepo.count();
    }

    private double toDouble(Object o) {
        if (o == null) return 0;
        if (o instanceof Number n) return Math.round(n.doubleValue() * 100.0) / 100.0;
        try { return Double.parseDouble(o.toString()); } catch (Exception e) { return 0; }
    }
}