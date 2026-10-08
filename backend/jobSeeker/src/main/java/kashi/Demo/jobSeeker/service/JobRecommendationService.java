package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.entity.Job;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobRecommendationService {

    public List<Job> recommendJobs(
            List<String> resumeSkills,
            List<Job> jobs) {

        Set<String> userSkills = resumeSkills.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Map<Job, Integer> jobScores = new HashMap<>();

        for (Job job : jobs) {

            String[] jobSkills = job.getTech();

            int matchedSkills = 0;

            if (jobSkills != null) {

                for (String skill : jobSkills) {

                    if (skill != null &&
                            userSkills.contains(skill.toLowerCase())) {

                        matchedSkills++;
                    }
                }
            }

            jobScores.put(job, matchedSkills);
        }

        return jobs.stream()
                .sorted((job1, job2) ->
                        Integer.compare(
                                jobScores.get(job2),
                                jobScores.get(job1)
                        )
                )
                .collect(Collectors.toList());
    }
}