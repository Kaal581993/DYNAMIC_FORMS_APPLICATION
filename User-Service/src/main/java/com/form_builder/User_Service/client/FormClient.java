package com.form_builder.User_Service.client;


import com.form_builder.User_Service.dto.FormDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "form-service", url = "${form.service.url:http://localhost:8082}")
public interface FormClient {

    @GetMapping("/api/forms/{formId}")
    FormDTO getForm(@PathVariable String formId);

}
