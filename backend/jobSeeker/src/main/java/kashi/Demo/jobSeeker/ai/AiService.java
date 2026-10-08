package kashi.Demo.jobSeeker.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final Client client;

    public AiService() {

        String apiKey = System.getenv("OPENAI_API_KEY");

        System.out.println("GEMINI API KEY FOUND: " + (apiKey != null));

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GEMINI_API_KEY environment variable is not set"
            );
        }

        client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String askAi(String question) {

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.5-flash-lite",
                        question,
                        null
                );

        return response.text();
    }
    public String extractResumeData(String resumeText) {

        String prompt = """
            Extract information from the following resume.

            Return ONLY valid JSON in exactly this format:

            {
              "skills": ["skill1", "skill2"],
              "experienceYears": 0,
              "education": "education details"
            }

            If experience is not mentioned, use 0.
            If education is not mentioned, use an empty string.

            Resume:
            """ + resumeText;

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.5-flash-lite",
                        prompt,
                        null
                );

        return response.text();
    }
}