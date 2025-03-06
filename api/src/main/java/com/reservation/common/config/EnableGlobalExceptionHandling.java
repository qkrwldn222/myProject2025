package com.reservation.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 클래스, 인터페이스, 열거형에 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임 시에도 유지
public @interface EnableGlobalExceptionHandling {}
