package com.reservation.common.converter;

import com.reservation.common.config.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

  @Override
  public String convertToDatabaseColumn(RoleType roleType) {
    if (roleType == null) {
      return null;
    }
    return roleType.getCode(); // Enum → DB (String) 변환
  }

  @Override
  public RoleType convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    return RoleType.fromCode(dbData); // DB (String) → Enum 변환
  }
}
