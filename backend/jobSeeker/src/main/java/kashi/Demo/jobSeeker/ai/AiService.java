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
}