package com.reservation.adapter.security.config;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Aspect
//@Component
@RequiredArgsConstructor
public class AuthorizationAspect {
    private final JwtTokenProvider jwtTokenProvider;

//    @Pointcut("execution(* com.reservation.adapter.*.*(..)) && !execution(* com.reservation.adapter.security.controller.AuthController.generateTempAdminToken(..))")
    public void restrictedControllers() {}

//    @Before("restrictedControllers()")
    public void checkAuthorization(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("JWT 토큰이 필요합니다.");
        }

        String jwt = token.substring(7);
        String userRole = jwtTokenProvider.getUserRoleFromToken(jwt);

        if (userRole == null) {
            throw new RuntimeException("권한이 없습니다.");
        }

        System.out.println("✅ 요청 API: " + joinPoint.getSignature().getName() + " | 권한: " + userRole);
    }
}
