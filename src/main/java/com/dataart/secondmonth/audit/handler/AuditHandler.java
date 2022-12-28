package com.dataart.secondmonth.audit.handler;

import com.dataart.secondmonth.audit.Audit;
import com.dataart.secondmonth.dto.AuditUserInfo;
import com.dataart.secondmonth.audit.persistence.document.AuditLog;
import com.dataart.secondmonth.audit.persistence.repository.AuditLogRepository;
import com.dataart.secondmonth.audit.processor.AuditProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
public abstract class AuditHandler {

    private final AuditLogRepository auditLogRepository;
    private final ModelMapper mapper;

    public abstract String getAuditName();

    public void log(ProceedingJoinPoint joinPoint) {
        final var signature = (MethodSignature) joinPoint.getSignature();
        final var method = signature.getMethod();

        final var auditDecoratorAnnotation = method.getAnnotation(Audit.class);

        final var title = auditDecoratorAnnotation.title();
        final var message = auditDecoratorAnnotation.message();
        final var logUserInfo = auditDecoratorAnnotation.logUserInfo();
        final var logParams = auditDecoratorAnnotation.logParams();
        final var projections = auditDecoratorAnnotation.projections();

        JsonNode parameters2Json = null;
        if (logParams) {
            final var argsNames = signature.getParameterNames();
            final var argsValues = joinPoint.getArgs();
            parameters2Json = AuditProcessor.parameters2Json(method.getParameterCount(), argsValues, argsNames, projections);
        }

        AuditUserInfo auditUserInfo = null;
        if (logUserInfo) {
            auditUserInfo = mapper.map(
                    SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getPrincipal(),
                    AuditUserInfo.class
            );
        }

        auditLogRepository.save(
                AuditLog.builder()
                        .title(title)
                        .message(message)
                        .methodName(method.getName())
                        .params(parameters2Json)
                        .userInfo(auditUserInfo)
                        .loggedAt(LocalDateTime.now())
                        .build()
        );
    }

}
