package com.dataart.secondmonth.audit.handler;

import com.dataart.secondmonth.audit.persistence.repository.AuditLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UpdatePostAuditHandler extends AuditHandler {

    public UpdatePostAuditHandler(AuditLogRepository auditLogRepository, ModelMapper mapper) {
        super(auditLogRepository, mapper);
    }

    @Override
    public String getAuditName() {
        return "update-post-audit";
    }

}
