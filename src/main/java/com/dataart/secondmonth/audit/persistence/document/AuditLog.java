package com.dataart.secondmonth.audit.persistence.document;

import com.dataart.secondmonth.dto.AuditUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class AuditLog {

    @Id
    private String id;

    private String title;

    private String methodName;

    private JsonNode params;

    private String message;

    private AuditUserInfo userInfo;

    private LocalDateTime loggedAt;

}
