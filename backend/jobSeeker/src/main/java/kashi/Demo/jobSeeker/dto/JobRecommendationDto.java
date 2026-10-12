package kashi.Demo.jobSeeker.dto;

import java.util.List;

public class JobRecommendationDto {

    private String jobId;
    private String profile;
    private String description;
    private int experience;
    private List<String> requiredSkills;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private double matchPercentage;

    public JobRecommendationDto(
            String jobId,
            String profile,
            String description,
            int experience,
            List<String> requiredSkills,
            List<String> matchedSkills,
            List<String> missingSkills,
            double matchPercentage
    ) {
        this.jobId = jobId;
        this.profile = profile;
        this.description = description;
        this.experience = experience;
        this.requiredSkills = requiredSkills;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.matchPercentage = matchPercentage;
    }

    public String getJobId() {
        return jobId;
    }

    public String getProfile() {
        return profile;
    }

    public String getDescription() {
        return description;
    }

    public int getExperience() {
        return experience;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }
}