package com.dataart.secondmonth.testcontainers.commons;

import com.dataart.secondmonth.dto.AuthRequestDto;
import com.dataart.secondmonth.dto.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TestRestClient {

    private final TestRestTemplate restTemplate;

    private HttpHeaders getAuthHeader(String token) {
        return Optional.ofNullable(token)
                .map(jwt -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(token));
                    return headers;
                })
                .orElse(null);
    }

    public ResponseEntity<JwtResponseDto> login(String restPath, String username, String password) {
        AuthRequestDto authRequestDto = new AuthRequestDto();
        authRequestDto.setUsername(username);
        authRequestDto.setPassword(password);

        return restTemplate.postForEntity(restPath, authRequestDto, JwtResponseDto.class);
    }

    public <T> ResponseEntity<T> get(String restPath, String jwt, Class<T> responseType) {
        return restTemplate.exchange(restPath, HttpMethod.GET, new HttpEntity<>(getAuthHeader(jwt)), responseType);
    }

    public <T> ResponseEntity<T> get(String restPath, String jwt, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(restPath, HttpMethod.GET, new HttpEntity<>(getAuthHeader(jwt)), responseType);
    }

    public <T> ResponseEntity<T> post(String restPath, String jwt, Object body, Class<T> responseType) {
        return restTemplate.exchange(restPath, HttpMethod.POST, new HttpEntity<>(body, getAuthHeader(jwt)), responseType);
    }

    public <T> ResponseEntity<T> post(String restPath, String jwt, Object body, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(restPath, HttpMethod.POST, new HttpEntity<>(body, getAuthHeader(jwt)), responseType);
    }

    public <T> ResponseEntity<T> put(String restPath, String jwt, Object body, Class<T> responseType) {
        return restTemplate.exchange(restPath, HttpMethod.PUT, new HttpEntity<>(body, getAuthHeader(jwt)), responseType);
    }

    public <T> ResponseEntity<T> put(String restPath, String jwt, Object body, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(restPath, HttpMethod.PUT, new HttpEntity<>(body, getAuthHeader(jwt)), responseType);
    }

    public <T> ResponseEntity<T> delete(String restPath, String jwt, Class<T> responseType) {
        return restTemplate.exchange(restPath, HttpMethod.DELETE, new HttpEntity<>(getAuthHeader(jwt)), responseType);
    }

}
