package com.reservation.common.config;


import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

//@ControllerAdvice
//@RestController
public class GlobalExceptionHandler {

    /**
     *  사용자가 직접 해결 가능한 오류 (ApiException)
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException ex, HttpServletRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    /**
     * 데이터베이스 관련 오류 처리 (MyBatis & JPA)
     */
    @ExceptionHandler({DataAccessException.class, PersistenceException.class, MyBatisSystemException.class})
    public ResponseEntity<Map<String, String>> handleDatabaseException(Exception ex, HttpServletRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "데이터베이스 오류");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 404 Not Found 예외 처리 (Swagger 포함)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "API 엔드포인트를 찾을 수 없습니다."));
    }

    /**
     * 기타 예외 처리 (서버 내부 오류)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex, HttpServletRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "서버 내부 오류 발생: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
