package com.form_builder.Form_Service.service;


import com.form_builder.Form_Service.dto.AddFieldRequest;
import com.form_builder.Form_Service.dto.CreateFormRequest;
import com.form_builder.Form_Service.model.FieldDefinition;
import com.form_builder.Form_Service.model.Form;
import com.form_builder.Form_Service.repository.FormRepository;
import jakarta.validation.Valid;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class FormService {

    private final FormRepository formRepository;

    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    public  Form createForm(@Valid CreateFormRequest request) {
        Form form = new Form();

        form.setName(request.getName());
        form.setDescription(request.getDescription());
        form.setFields(new ArrayList<>());
        form.setVersion(1);
        form.setActive(true);
        form.setCreatedAt(LocalDateTime.now());

        return formRepository.save(form);
    }

    public Form addField(String formId, @Valid AddFieldRequest request) {

        Form form = formRepository.findById(formId).orElseThrow(
                ()-> new RuntimeException("Form not found")
        );

        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setId(UUID.randomUUID().toString());
        fieldDefinition.setLabel(request.getLabel());
        fieldDefinition.setName(request.getName());
        fieldDefinition.setConfig(request.getConfig());
        fieldDefinition.setRequired(request.isRequired());
        fieldDefinition.setType(request.getType());

        form.getFields().add(fieldDefinition);
        return formRepository.save(form);

    }
}
