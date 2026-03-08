package com.form_builder.Submission_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {

    private String id;

    private String formId;

    private String submittedBy;

    private LocalDateTime submittedAt;

    private Map<String, Object> data;
}