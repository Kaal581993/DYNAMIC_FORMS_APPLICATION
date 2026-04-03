package com.form_builder.Submission_Service.model;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection="submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    private String Id;

    private String formId;

    private String submittedBy;
    private String submittedByEmail;
    private String recipientEmail;

    private LocalDateTime submittedAt;

    private Map<String, Object> data;
}
