package com.dataart.secondmonth.config.security.oauth2.user;

import com.dataart.secondmonth.exception.OAuth2AuthenticationProcessingException;

import java.util.Locale;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase(Locale.ROOT)) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "github" -> new GithubOAuth2UserInfo(attributes);
            default -> throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        };
    }
}
