package com.form_builder.Submission_Service.repository;


import com.form_builder.Submission_Service.dto.FormDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "form-service")

public interface FormClient {
    @GetMapping("/api/forms/{formId}")
     FormDTO getForm(@PathVariable String formId);

}
