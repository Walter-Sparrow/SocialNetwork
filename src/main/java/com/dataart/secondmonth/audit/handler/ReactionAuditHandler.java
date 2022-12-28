package com.dataart.secondmonth.audit.handler;

import com.dataart.secondmonth.audit.persistence.repository.AuditLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReactionAuditHandler extends AuditHandler {

    public ReactionAuditHandler(AuditLogRepository auditLogRepository, ModelMapper mapper) {
        super(auditLogRepository, mapper);
    }

    @Override
    public String getAuditName() {
        return "reaction-audit";
    }

}
