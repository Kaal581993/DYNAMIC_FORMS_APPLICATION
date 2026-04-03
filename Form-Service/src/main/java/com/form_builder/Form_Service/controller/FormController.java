package com.form_builder.Form_Service.controller;

import com.form_builder.Form_Service.dto.AddFieldRequest;
import com.form_builder.Form_Service.dto.CreateFormRequest;
import com.form_builder.Form_Service.model.FieldType;
import com.form_builder.Form_Service.model.Form;
import com.form_builder.Form_Service.service.FormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController()
@RequestMapping("api/forms")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form>  createForm(
            @Valid @RequestBody CreateFormRequest request
    ){
        return ResponseEntity.ok(formService.createForm(request));

    }

    @GetMapping("/field-types")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getFieldTypes() {
        List<String> fieldTypes = Arrays.stream(FieldType.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(fieldTypes);
    }

    @PostMapping("/{formId}/fields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addField(
            @PathVariable String formId,
            @Valid @RequestBody
            AddFieldRequest request
    ){
        return ResponseEntity.ok(
                formService.addField(formId, request)
        );
    }

    @PostMapping("/{formId}/fields/text")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addTextField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.TEXT);
    }

    @PostMapping("/{formId}/fields/textarea")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addTextareaField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.TEXTAREA);
    }

    @PostMapping("/{formId}/fields/encryptedtextbox")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addEncryptedTextboxField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.ENCRYPTEDTEXTBOX);
    }

    @PostMapping("/{formId}/fields/date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addDateField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.DATE);
    }

    @PostMapping("/{formId}/fields/radio")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addRadioField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.RADIO);
    }

    @PostMapping("/{formId}/fields/checkbox")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addCheckboxField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.CHECKBOX);
    }

    @PostMapping("/{formId}/fields/dropdown")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addDropdownField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.DROPDOWN);
    }

    @PostMapping("/{formId}/fields/wysiwyg")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addWysiwygField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.WYSIWYG);
    }

    @PostMapping("/{formId}/fields/rtf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addRtfField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.RTF);
    }

    @PostMapping("/{formId}/fields/code")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Form> addCodeField(
            @PathVariable String formId,
            @Valid @RequestBody AddFieldRequest request
    ) {
        return addTypedField(formId, request, FieldType.CODE);
    }

    @GetMapping("/{formId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Form> getForm(@PathVariable String formId) {
        return ResponseEntity.ok(formService.getForm(formId));
    }

    private ResponseEntity<Form> addTypedField(String formId, AddFieldRequest request, FieldType type) {
        request.setType(type);
        return ResponseEntity.ok(formService.addField(formId, request));
    }
}
