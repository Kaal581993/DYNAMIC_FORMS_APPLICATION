package com.form_builder.Form_Service.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection="Form")
public class Form {
    @Id
    private String id;
    private String name;
    private String description;

    private List<FieldDefinition> fields;

    private Integer version;
    private Boolean active;

    private LocalDateTime createdAt;
}
