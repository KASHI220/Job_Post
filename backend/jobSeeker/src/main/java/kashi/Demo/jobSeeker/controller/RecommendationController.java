package kashi.Demo.jobSeeker.controller;

import kashi.Demo.jobSeeker.entity.Job;
import kashi.Demo.jobSeeker.service.JobRecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final JobRecommendationService recommendationService;

    public RecommendationController(JobRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/test")
    public List<Job> testRecommendation(
            @RequestBody List<String> skills) {

        // For now, use your existing jobs here.
        return recommendationService.recommendJobs(
                skills,
                List.of()
        );
    }
}