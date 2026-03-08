package com.form_builder.Submission_Service.repository;

import com.form_builder.Submission_Service.dto.SubmissionResponse;
import com.form_builder.Submission_Service.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<SubmissionResponse> findByFormId(String formId);
}
