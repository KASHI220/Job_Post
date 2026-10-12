package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.dto.JobRecommendationDto;
import kashi.Demo.jobSeeker.dto.ResumeDataDto;
import kashi.Demo.jobSeeker.entity.Job;
import kashi.Demo.jobSeeker.repository.SearchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class JobRecommendationService {

    private final SearchRepository searchRepository;

    public JobRecommendationService(
            SearchRepository searchRepository
    ) {
        this.searchRepository = searchRepository;
    }

    public List<JobRecommendationDto> recommendJobs(
            ResumeDataDto resumeData
    ) {

        if (resumeData == null) {
            throw new IllegalArgumentException(
                    "Resume data cannot be null"
            );
        }

        // Step 1: Normalize the skills extracted from the resume.
        Set<String> candidateSkills =
                normalizeSkills(resumeData.getSkills());

        // Step 2: Retrieve jobs from MongoDB.
       List<Job> jobs = searchRepository.findAllJobs();;

        List<JobRecommendationDto> recommendations =
                new ArrayList<>();

        // Step 3: Compare the resume against every job.
        for (Job job : jobs) {

            List<String> requiredSkills =
                    getUniqueSkills(job.getTech());

            List<String> matchedSkills = new ArrayList<>();
            List<String> missingSkills = new ArrayList<>();

            for (String skill : requiredSkills) {

                if (candidateSkills.contains(
                        skill.toLowerCase(Locale.ROOT)
                )) {
                    matchedSkills.add(skill);
                } else {
                    missingSkills.add(skill);
                }
            }

            // Step 4: Calculate the match percentage.
            double matchPercentage = 0;

            if (!requiredSkills.isEmpty()) {

                matchPercentage = Math.round(
                        (double) matchedSkills.size()
                                / requiredSkills.size()
                                * 100
                );
            }

            // Step 5: Build the recommendation response.
            JobRecommendationDto recommendation =
                    new JobRecommendationDto(
                            job.getId(),
                            job.getProfile(),
                            job.getDesc(),
                            job.getExp(),
                            requiredSkills,
                            matchedSkills,
                            missingSkills,
                            matchPercentage
                    );

            recommendations.add(recommendation);
        }

        // Step 6: Put the highest-scoring jobs first.
        recommendations.sort(
                Comparator.comparingDouble(
                        JobRecommendationDto::getMatchPercentage
                ).reversed()
        );

        return recommendations;
    }

    private Set<String> normalizeSkills(
            List<String> skills
    ) {

        Set<String> normalizedSkills = new LinkedHashSet<>();

        if (skills == null) {
            return normalizedSkills;
        }

        for (String skill : skills) {

            if (skill != null && !skill.isBlank()) {

                normalizedSkills.add(
                        skill.trim().toLowerCase(Locale.ROOT)
                );
            }
        }

        return normalizedSkills;
    }

    private List<String> getUniqueSkills(
            String[] skills
    ) {

        Map<String, String> uniqueSkills =
                new LinkedHashMap<>();

        if (skills == null) {
            return new ArrayList<>();
        }

        for (String skill : skills) {

            if (skill != null && !skill.isBlank()) {

                String cleanedSkill = skill.trim();

                uniqueSkills.putIfAbsent(
                        cleanedSkill.toLowerCase(Locale.ROOT),
                        cleanedSkill
                );
            }
        }

        return new ArrayList<>(uniqueSkills.values());
    }
}