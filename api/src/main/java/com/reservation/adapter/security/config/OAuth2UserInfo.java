package com.reservation.adapter.security.config;

import java.util.Map;

public abstract class OAuth2UserInfo {
  protected Map<String, Object> attributes;

  public OAuth2UserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public abstract String getId();

  public abstract String getName();

  public abstract String getEmail();

  public abstract String getProvider();
}
