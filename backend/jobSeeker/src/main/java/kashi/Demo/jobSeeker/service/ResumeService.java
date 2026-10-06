package kashi.Demo.jobSeeker.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeService {

    public String extractText(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        try {
            byte[] fileBytes = file.getBytes();

            try (PDDocument document = Loader.loadPDF(fileBytes)) {

                PDFTextStripper stripper = new PDFTextStripper();

                return stripper.getText(document);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read PDF", e);
        }
    }
}
