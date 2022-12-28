package com.dataart.secondmonth.audit;

import com.dataart.secondmonth.audit.handler.AuditProxyHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final ApplicationContext applicationContext;
    private final AuditProxyHandler proxyHandler;

    @Around("@annotation(Audit)")
    @SneakyThrows
    public Object logMessage(ProceedingJoinPoint joinPoint) {
        final var signature = (MethodSignature) joinPoint.getSignature();
        final var method = signature.getMethod();
        final var auditDecoratorAnnotation = method.getAnnotation(Audit.class);
        final var handlerClass = auditDecoratorAnnotation.handler();
        final var handler = applicationContext.getBean(handlerClass);

        proxyHandler.setHandler(handler);
        proxyHandler.log(joinPoint);

        return joinPoint.proceed();
    }

}
