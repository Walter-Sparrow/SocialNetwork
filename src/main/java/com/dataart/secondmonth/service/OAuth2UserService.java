package com.dataart.secondmonth.service;

import com.dataart.secondmonth.config.security.oauth2.user.OAuth2UserInfo;
import com.dataart.secondmonth.config.security.oauth2.user.OAuth2UserInfoFactory;
import com.dataart.secondmonth.dto.OAuth2UserDto;
import com.dataart.secondmonth.exception.OAuth2AuthenticationProcessingException;
import com.dataart.secondmonth.persistence.entity.User;
import com.dataart.secondmonth.persistence.repository.AuthProviderRepository;
import com.dataart.secondmonth.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository repository;
    private final AuthProviderRepository providerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        final var user = super.loadUser(userRequest);
        return processOAuth2User(userRequest, user);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        repository.findByEmailIgnoreCase(oAuth2UserInfo.getEmail())
                .ifPresentOrElse((user) -> {
                    if (!user.getAuthProvider().getName().equals(oAuth2UserRequest.getClientRegistration().getRegistrationId())) {
                        throw new OAuth2AuthenticationProcessingException(
                                "Looks like you're signed up with %s account. Please use your %s account to login."
                                        .formatted(user.getAuthProvider().getName(), user.getAuthProvider().getName()));
                    }
                }, () -> registerNewUser(oAuth2UserRequest, oAuth2UserInfo));

        return new OAuth2UserDto(oAuth2User);
    }

    private void registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        repository.save(
                User.builder()
                        .username(oAuth2UserInfo.getName())
                        .authProvider(providerRepository.getByName(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                        .email(oAuth2UserInfo.getEmail())
                        .enabled(true)
                        .createdAt(ZonedDateTime.now())
                        .build()
        );
    }

}
