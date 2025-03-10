package com.reservation.adapter.security.config;

import com.reservation.adapter.security.mapper.SecurityRequestMapper;
import com.reservation.application.user.model.SignupCommand;
import com.reservation.application.user.service.UserService;
import com.reservation.common.enums.RoleType;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserService userService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

    // 제공자의 ID 가져오기 (Google, Kakao, Naver)
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttribute =
        userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

    Map<String, Object> attributes = oAuth2User.getAttributes();
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

    SignupCommand command = SecurityRequestMapper.INSTANCE.toCommand(userInfo);

    // DB에 사용자 저장
    userService.registerUser(command);

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(RoleType.USER.getCode())),
        attributes,
        userNameAttribute);
  }
}
