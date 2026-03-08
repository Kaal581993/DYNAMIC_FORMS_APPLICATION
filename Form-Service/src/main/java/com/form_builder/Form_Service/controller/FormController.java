package com.form_builder.Form_Service.controller;

import com.form_builder.Form_Service.dto.AddFieldRequest;
import com.form_builder.Form_Service.dto.CreateFormRequest;
import com.form_builder.Form_Service.model.Form;
import com.form_builder.Form_Service.service.FormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/forms")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;

    @PostMapping
    public ResponseEntity<Form>  createForm(
            @Valid @RequestBody CreateFormRequest request
    ){
        return ResponseEntity.ok(formService.createForm(request));

    }

    @PostMapping("/{formId}/fields")
    public ResponseEntity<Form> addField(
            @PathVariable String formId,
            @Valid @RequestBody
            AddFieldRequest request
    ){
        return ResponseEntity.ok(
                formService.addField(formId, request)
        );
    }
}
