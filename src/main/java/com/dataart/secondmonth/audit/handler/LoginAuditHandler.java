package com.dataart.secondmonth.audit.handler;

import com.dataart.secondmonth.audit.persistence.repository.AuditLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LoginAuditHandler extends AuditHandler {

    public LoginAuditHandler(AuditLogRepository auditLogRepository, ModelMapper mapper) {
        super(auditLogRepository, mapper);
    }

    @Override
    public String getAuditName() {
        return "login-audit";
    }

}
