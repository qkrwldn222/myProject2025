package com.reservation.common.config;

import jakarta.persistence.PersistenceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ExceptionAspect {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);

    @Around("within(@com.reservation.common.config.EnableGlobalExceptionHandling *)")
    public Object handleControllerExceptions(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed(); // 원래 메서드 실행
        } catch (ApiException ex) {
            // API 예외 처리 (사용자가 해결할 수 있는 오류)
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return ResponseEntity.status(ex.getStatus()).body(errorResponse);
        } catch (DataAccessException | PersistenceException  ex) {
            logger.info(ex.getMessage());
            // DB 관련 예외 처리
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "데이터베이스 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Throwable ex) {
            logger.info(ex.getMessage());
            // 기타 예외 처리
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "서버 내부 오류 발생: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
