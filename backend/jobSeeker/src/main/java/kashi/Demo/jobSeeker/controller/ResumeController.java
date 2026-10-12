package kashi.Demo.jobSeeker.controller;

import kashi.Demo.jobSeeker.dto.ResumeDataDto;
import kashi.Demo.jobSeeker.service.ResumeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResumeDataDto> uploadResume(
            @RequestParam("file") MultipartFile file) {

        ResumeDataDto resumeData =
                resumeService.extractResumeData(file);

        return ResponseEntity.ok(resumeData);
    }
}