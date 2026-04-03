package com.form_builder.Submission_Service.validator;

import com.form_builder.Submission_Service.dto.FieldDTO;
import com.form_builder.Submission_Service.dto.FormDTO;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SubmissionValidator {

    public static void validate(
            FormDTO form, Map<String, Object> submissionData
    ){

        Set<String> validFields = form.getFields()
                .stream()
                .map(FieldDTO::getId)
                .collect(Collectors.toSet());

        for (String field : submissionData.keySet()) {

            if (!validFields.contains(field)) {
                throw new RuntimeException("Invalid field: " + field);
            }
        }

        for (FieldDTO field : form.getFields()) {

            if (field.isRequired()
                    && !submissionData.containsKey(field.getId())) {

                throw new RuntimeException(
                        "Required field missing: " + field.getId());
            }
        }

        }
}
