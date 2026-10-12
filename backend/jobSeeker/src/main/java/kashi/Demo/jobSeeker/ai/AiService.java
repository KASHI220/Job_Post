package kashi.Demo.jobSeeker.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import kashi.Demo.jobSeeker.dto.ResumeDataDto;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final Client client;
    private final Gson gson = new Gson();

    public AiService() {

        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "OPENAI_API_KEY environment variable is not set"
            );
        }

        client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    // Existing method for asking Gemini questions
    public String askAi(String question) {

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.5-flash-lite",
                        question,
                        null
                );

        return response.text();
    }

    // Extract structured information from a resume
    public ResumeDataDto extractResumeData(String resumeText) {

        if (resumeText == null || resumeText.isBlank()) {
            throw new IllegalArgumentException(
                    "Resume text cannot be empty"
            );
        }

        String prompt = """
                Analyze the following resume.

                Extract the candidate's technical skills,
                total professional work experience in years,
                and education.

                Return ONLY valid JSON in this format:

                {
                  "skills": ["Java", "Spring Boot", "SQL"],
                  "experienceYears": 0,
                  "education": "Bachelor of Engineering"
                }

                Rules:
                1. Include only skills supported by the resume.
                2. Do not assume professional experience from projects.
                3. If professional experience is not mentioned,
                   use 0 for experienceYears.
                4. If education is not mentioned, use an empty string.
                5. Do not include Markdown or explanations.

                Resume:
                """ + resumeText;

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.5-flash-lite",
                        prompt,
                        null
                );

        String json = response.text();

        if (json == null || json.isBlank()) {
            throw new IllegalStateException(
                    "Gemini returned an empty response"
            );
        }

        // Remove Markdown code fences if Gemini includes them.
        json = json.trim()
                .replaceFirst("^```(?:json)?\\s*", "")
                .replaceFirst("\\s*```$", "");

        try {

            ResumeDataDto resumeData =
                    gson.fromJson(json, ResumeDataDto.class);

            if (resumeData == null) {
                throw new IllegalStateException(
                        "Gemini returned an empty JSON object"
                );
            }

            return resumeData;

        } catch (JsonSyntaxException e) {

            throw new IllegalStateException(
                    "Failed to parse Gemini's JSON response",
                    e
            );
        }
    }
}