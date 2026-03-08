package com.form_builder.Submission_Service.service;

import com.form_builder.Submission_Service.dto.FieldDTO;
import com.form_builder.Submission_Service.dto.FormDTO;
import com.form_builder.Submission_Service.dto.SubmissionResponse;
import com.form_builder.Submission_Service.dto.SubmitFormRequest;
import com.form_builder.Submission_Service.model.Submission;
import com.form_builder.Submission_Service.repository.FormClient;
import com.form_builder.Submission_Service.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {

   private final SubmissionRepository submissionRepository;


   private final FormClient formClient;

    public  Submission submitForm(SubmitFormRequest request, String userId) {

        FormDTO formDTO = formClient.getForm(request.getFormId());

        validateFields(formDTO, request.getData());

        Submission submission = Submission.builder()
                .formId(request.getFormId())
                .submittedBy(userId)
                .submittedAt(LocalDateTime.now())
                .data(request.getData())
                .build();

        return submissionRepository.save(submission);


    }

    private void validateFields(FormDTO formDTO, Map<String, Object> data) {
        Set<String> validFields = formDTO.getFields()
                .stream()
                .map(FieldDTO::getId)
                .collect(Collectors.toSet());

        for (String key : data.keySet()) {

            if (!validFields.contains(key)) {
                throw new RuntimeException(
                        "Invalid field: " + key);
            }

        }
    }


    public  List<SubmissionResponse> getSubmissions(String formId) {

        List<SubmissionResponse> submissionResponse = submissionRepository.findByFormId(formId);

        return submissionResponse.stream().map(this::mapToResponse).collect(Collectors.toList());


}

    private SubmissionResponse mapToResponse(SubmissionResponse submissionResponse) {

        SubmissionResponse response = new SubmissionResponse();
        response.setFormId(submissionResponse.getFormId());
        response.setData(submissionResponse.getData());
        response.setSubmittedAt(submissionResponse.getSubmittedAt());
        response.setSubmittedBy(submissionResponse.getSubmittedBy());

        return  response;

    }
    }
