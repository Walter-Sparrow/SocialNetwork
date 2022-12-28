package com.dataart.secondmonth.audit.handler;

import com.dataart.secondmonth.audit.persistence.repository.AuditConfigRepository;
import com.dataart.secondmonth.audit.persistence.repository.AuditLogRepository;
import com.dataart.secondmonth.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AuditProxyHandler extends AuditHandler {

    private final AuditConfigRepository auditConfigRepository;
    private AuditHandler handler;

    public AuditProxyHandler(
            AuditLogRepository auditLogRepository,
            ModelMapper mapper,
            AuditConfigRepository auditConfigRepository) {
        super(auditLogRepository, mapper);
        this.auditConfigRepository = auditConfigRepository;
    }

    public void setHandler(AuditHandler handler) {
        this.handler = handler;
    }

    @Override
    public void log(ProceedingJoinPoint joinPoint) {
        Optional.ofNullable(handler)
                .orElseThrow(() -> {
                    throw new NullPointerException("Audit proxy didn't receive a handler.");
                });

        final var config = auditConfigRepository.findById(getAuditName())
                .orElseThrow(() -> {
                    throw new NotFoundException("Audit with name %s does not exist.".formatted(getAuditName()));
                });

        if (!config.getEnabled()) {
            log.info("{} is disabled.", getAuditName());
            return;
        }

        handler.log(joinPoint);
    }

    @Override
    public String getAuditName() {
        return handler.getAuditName();
    }

}
