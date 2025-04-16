package com.fintech.banking.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class RequestLoggingAspect {

    private final ObjectMapper objectMapper;

    public RequestLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {
    }

    @Before("restControllerMethods()")
    public void logRequest(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String clientIp = request.getRemoteAddr();
        String timestamp = LocalDateTime.now().toString();

        Object[] args = joinPoint.getArgs();
        String body = extractRequestBody(joinPoint);

        log.info("""
                Incoming Request
                Time     : {}
                Method   : {} {}
                Client IP: {}
                Body     : {}
                """, timestamp, method, uri, clientIp, body);
    }

    private String extractRequestBody(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : paramAnnotations[i]) {
                if (annotation instanceof RequestBody && args[i] != null) {
                    try {
                        String json = objectMapper.writeValueAsString(args[i]);
                        return maskAmountAndBalance(json);
                    } catch (Exception e) {
                        return "Could not serialize request body: " + e.getMessage();
                    }
                }
            }
        }
        return "N/A";
    }

    private String maskAmountAndBalance(String json) {
        return json
                .replaceAll("(?i)(\"amount\"\\s*:\\s*)\\d+(\\.\\d+)?", "$1****")
                .replaceAll("(?i)(\"balance\"\\s*:\\s*)\\d+(\\.\\d+)?", "$1****");
    }
}