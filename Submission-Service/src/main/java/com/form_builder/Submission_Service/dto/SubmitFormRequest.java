package com.form_builder.Submission_Service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;



@Data
public class SubmitFormRequest {

    @NotBlank
    private String formId;

    private Map<String, Object> data;

    private String recipientEmail;
}
