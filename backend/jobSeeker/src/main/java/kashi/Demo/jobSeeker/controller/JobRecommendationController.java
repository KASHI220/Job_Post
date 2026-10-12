package kashi.Demo.jobSeeker.controller;

import kashi.Demo.jobSeeker.dto.JobRecommendationDto;
import kashi.Demo.jobSeeker.dto.ResumeDataDto;
import kashi.Demo.jobSeeker.service.JobRecommendationService;
import kashi.Demo.jobSeeker.service.ResumeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class JobRecommendationController {

    private final ResumeService resumeService;
    private final JobRecommendationService recommendationService;

    public JobRecommendationController(
            ResumeService resumeService,
            JobRecommendationService recommendationService
    ) {
        this.resumeService = resumeService;
        this.recommendationService = recommendationService;
    }

    @PostMapping(
            value = "/resume",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<JobRecommendationDto>>
    recommendJobsFromResume(
            @RequestParam("file") MultipartFile file
    ) {

        // Extract skills, experience, and education using Gemini.
        ResumeDataDto resumeData =
                resumeService.extractResumeData(file);

        // Compare the extracted skills against MongoDB jobs.
        List<JobRecommendationDto> recommendations =
                recommendationService.recommendJobs(resumeData);

        return ResponseEntity.ok(recommendations);
    }
}