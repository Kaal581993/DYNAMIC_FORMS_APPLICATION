package com.form_builder.Submission_Service.controller;


import com.form_builder.Submission_Service.dto.SubmissionResponse;
import com.form_builder.Submission_Service.dto.SubmitFormRequest;
import com.form_builder.Submission_Service.model.Submission;
import com.form_builder.Submission_Service.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/submission")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<Submission> submitForm(
            @RequestBody SubmitFormRequest request,
            @RequestHeader("X-USER-ID") String userId
    ){
        return ResponseEntity.ok(
                submissionService.submitForm(request, userId)
        );


    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissions(
            @PathVariable String formId) {

        return ResponseEntity.ok(
                submissionService.getSubmissions(formId)
        );
    }


}
