package dev.animals.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum GenderType {
  M, F;

  public static GenderType getOf(String gender) {
    if (StringUtils.isBlank(gender)) {
      return null;
    }
    return Arrays.stream(GenderType.values())
        .filter(value -> value.name().equalsIgnoreCase(gender))
        .findAny()
        .orElse(null);
  }
}
