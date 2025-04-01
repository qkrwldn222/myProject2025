package com.reservation.common.utils;

import com.reservation.common.enums.DayOfWeekEnum;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

public class DateTimeUtils {
  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DateTimeUtils.class);
  // 기본 날짜/시간 포맷
  public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DEFAULT_DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
  public static final String DEFAULT_DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

  private DateTimeUtils() {
    // 유틸 클래스의 인스턴스화를 방지
  }

  /** 문자열을 LocalDateTime으로 변환 (기본 포맷 사용) */
  public static LocalDateTime parseDateTime(String dateTimeStr) {
    return parseDateTime(dateTimeStr, DEFAULT_DATE_TIME_FORMAT);
  }

  /** 문자열을 LocalDateTime으로 변환 (사용자 정의 포맷) */
  public static LocalDateTime parseDateTime(String dateTimeStr, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return LocalDateTime.parse(dateTimeStr, formatter);
  }

  /** 문자열을 LocalDate로 변환 (기본 포맷 사용) */
  public static LocalDate parseDate(String dateStr) {
    return parseDate(dateStr, DEFAULT_DATE_FORMAT_YYYY_MM_DD);
  }

  /** 문자열을 LocalDate로 변환 (사용자 정의 포맷) */
  public static LocalDate parseDate(String dateStr, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return LocalDate.parse(dateStr, formatter);
  }

  /** LocalDateTime을 문자열로 변환 (기본 포맷 사용) */
  public static String formatDateTime(LocalDateTime dateTime) {
    return formatDateTime(dateTime, DEFAULT_DATE_TIME_FORMAT);
  }

  /** LocalDateTime을 문자열로 변환 (사용자 정의 포맷) */
  public static String formatDateTime(LocalDateTime dateTime, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return dateTime.format(formatter);
  }

  /** LocalDate를 문자열로 변환 (기본 포맷 사용) */
  public static String formatDate(LocalDate date) {
    return formatDate(date, DEFAULT_DATE_FORMAT_YYYY_MM_DD);
  }

  /** LocalDate를 문자열로 변환 (사용자 정의 포맷) */
  public static String formatDate(LocalDate date, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return date.format(formatter);
  }

  public static LocalDateTime toLocalDateTime(String dateStr) {
    return parseDateTime(dateStr, DEFAULT_DATE_TIME_FORMAT);
  }

  public static LocalDateTime toLocalDateTime(String dateStr, String format) {
    return parseDateTime(dateStr, format);
  }

  public static LocalDateTime toLocalDateTime(LocalDate localDate, LocalTime localTime) {
    return LocalDateTime.of(localDate, localTime);
  }

  public static LocalDate safeParseDate(String dateStr) {
    if (!StringUtils.hasText(dateStr)) { // 빈 값이면 null 반환
      return null;
    }
    if (!StringUtils.hasText(dateStr)) {
      return null;
    }

    String trimmed = dateStr.trim(); // 공백 제거

    try {
      if (trimmed.matches("^\\d{8}$")) { // yyyyMMdd (예: 19661028)
        return LocalDate.parse(trimmed, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_YYYYMMDD));
      } else if (trimmed.matches("^\\d{4}-\\d{2}-\\d{2}$")) { // yyyy-MM-dd (예: 2002-07-30)
        return LocalDate.parse(
            trimmed, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_YYYY_MM_DD));
      } else if (trimmed.matches(
          "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) { // yyyy-MM-dd HH:mm:ss (예: 2002-07-30
        // 00:00:00)
        return LocalDate.parse(
            trimmed.substring(0, 10), DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_YYYY_MM_DD));
      }
    } catch (DateTimeParseException e) {
      logger.warn("Invalid date format: '{}' - Unable to parse", dateStr);
    }
    return null; // 변환 실패 시 null 반환
  }

  /** 특정 날짜가 예약 가능 범위에 포함되는지 검증 */
  public static boolean isDateWithinAvailableRange(
      Map<String, String> dateConfig, LocalDate targetDate) {
    if (dateConfig == null || dateConfig.isEmpty()) return true;

    String type = dateConfig.get("type");

    if ("DAYS_AFTER".equals(type)) {
      int daysAfter = Integer.parseInt(dateConfig.get("days"));
      LocalDate startDate = LocalDate.now();
      LocalDate endDate = startDate.plusDays(daysAfter);
      return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    } else if ("FIXED_RANGE".equals(type)) {
      LocalDate startDate = parseDate(dateConfig.get("start_date"));
      LocalDate endDate = parseDate(dateConfig.get("end_date"));
      return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    return false;
  }

  public static DayOfWeekEnum getDayOfWeekEnum(LocalDate reservationDate) {
    // Java 기본 DayOfWeek (MONDAY~SUNDAY)
    DayOfWeek dayOfWeek = reservationDate.getDayOfWeek();

    // Java DayOfWeek을 우리가 만든 DayOfWeekEnum으로 변환
    return switch (dayOfWeek) {
      case MONDAY -> DayOfWeekEnum.MON;
      case TUESDAY -> DayOfWeekEnum.TUE;
      case WEDNESDAY -> DayOfWeekEnum.WED;
      case THURSDAY -> DayOfWeekEnum.THU;
      case FRIDAY -> DayOfWeekEnum.FRI;
      case SATURDAY -> DayOfWeekEnum.SAT;
      case SUNDAY -> DayOfWeekEnum.SUN;
    };
  }
}
