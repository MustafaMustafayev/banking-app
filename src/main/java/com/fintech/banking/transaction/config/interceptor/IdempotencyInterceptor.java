package com.fintech.banking.transaction.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fintech.banking.transaction.dto.response.TransactionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class IdempotencyInterceptor implements HandlerInterceptor {

    private static final Map<String, TransactionResponseDto> cache = new ConcurrentHashMap<>();

    /*
    we can also check if request with same idempotency key but different body,
    then should return conflict.
    also we can check same idempotency key for different actions, return also conflict
    */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String idempotencyHeaderKey = request.getHeader("X-Idempotency-Key");

        if (idempotencyHeaderKey != null && cache.containsKey(idempotencyHeaderKey)) {
            log.info("💡 Idempotent response reused for key: {}", idempotencyHeaderKey);

            TransactionResponseDto cachedResponse = cache.get(idempotencyHeaderKey);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Idempotency-Cache-Hit", "true");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            //if optimistic lock happened, response can be
            if(cachedResponse != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                String json = objectMapper.writeValueAsString(cachedResponse);
                response.getWriter().write(json);
                response.getWriter().flush();
            }
            else{
                response.getWriter().write("Your request is in progress, will retry");
            }
            return false; // 🚫 stop further processing
        }

        return true; // ✅ let it continue
    }

    public static void storeResponse(String key,  TransactionResponseDto data) {
        cache.put(key, data);
    }
}