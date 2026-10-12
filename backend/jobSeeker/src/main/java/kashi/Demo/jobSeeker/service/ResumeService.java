package kashi.Demo.jobSeeker.service;

import kashi.Demo.jobSeeker.ai.AiService;
import kashi.Demo.jobSeeker.dto.ResumeDataDto;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeService {

    private final AiService aiService;

    public ResumeService(AiService aiService) {
        this.aiService = aiService;
    }

    public ResumeDataDto extractResumeData(MultipartFile file) {

        // Validate the uploaded file.
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "File is empty"
            );
        }

        // Validate the file type.
        if (!"application/pdf".equalsIgnoreCase(
                file.getContentType())) {

            throw new IllegalArgumentException(
                    "Only PDF files are allowed"
            );
        }

        try {

            byte[] fileBytes = file.getBytes();

            // Extract text from the PDF.
            try (PDDocument document =
                         Loader.loadPDF(fileBytes)) {

                PDFTextStripper stripper =
                        new PDFTextStripper();

                String resumeText =
                        stripper.getText(document);

                if (resumeText == null ||
                        resumeText.isBlank()) {

                    throw new IllegalArgumentException(
                            "No readable text found in the PDF"
                    );
                }

                // Send extracted text to Gemini.
                return aiService.extractResumeData(
                        resumeText
                );
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to read the PDF file",
                    e
            );
        }
    }
}