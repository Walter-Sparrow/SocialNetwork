package com.dataart.secondmonth.testcontainers;

import com.dataart.secondmonth.dto.ConfirmationTokenDto;
import com.dataart.secondmonth.dto.JwtResponseDto;
import com.dataart.secondmonth.dto.RegistrationUserDto;
import com.dataart.secondmonth.dto.UserDto;
import com.dataart.secondmonth.service.ConfirmationTokenService;
import com.dataart.secondmonth.testcontainers.commons.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthControllerTests extends AbstractTest {

    private static final String USERNAME = "test";
    private static final String PASSWORD = "TestTest123!@#";

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Test
    public void loginWithRightCredentialsTest() {
        ResponseEntity<JwtResponseDto> response = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        final var jwtResponseDto = response.getBody();
        assertNotNull(jwtResponseDto);
        assertNotNull(jwtResponseDto.getToken());
    }

    @Test
    public void loginWithWrongCredentialsTest() {
        ResponseEntity<JwtResponseDto> response = testRestTemplate.login(getUrlWithEndpoint("/auth"), "$NONEXISTINGUSER$", PASSWORD);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void registerValidTest() {
        final var registrationUserDto = RegistrationUserDto.builder()
                .username("TEST_USER")
                .email("test@validmail.com")
                .password("TestTest123!@#")
                .confirmPassword("TestTest123!@#")
                .firstName("test")
                .middleName("test")
                .lastName("test")
                .birthday(LocalDate.of(1970, 1, 1))
                .build();

        ResponseEntity<UserDto> response = testRestTemplate.post(getUrlWithEndpoint("/registration"), null, registrationUserDto, UserDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        final var user = response.getBody();
        assertNotNull(user);
        assertNotNull(user.id);
        assertEquals(registrationUserDto.username, user.username);
        assertEquals(registrationUserDto.email, user.email);
        assertEquals(registrationUserDto.firstName, user.firstName);
        assertEquals(registrationUserDto.middleName, user.middleName);
        assertEquals(registrationUserDto.lastName, user.lastName);
        assertEquals(registrationUserDto.birthday, user.birthday);
    }

    @Test
    public void registerWithMismatchedConfirmationPasswordTest() {
        final var registrationUserDto = RegistrationUserDto.builder()
                .username("TEST_USER")
                .email("test@validmail.com")
                .password("TestTest123!@#")
                .confirmPassword("#@!321TestTest")
                .build();

        ResponseEntity<UserDto> response = testRestTemplate.post(getUrlWithEndpoint("/registration"), null, registrationUserDto, UserDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void registerWithExistingUsernameTest() {
        final var registrationUserDto = RegistrationUserDto.builder()
                .username(USERNAME)
                .email("test@validmail.com")
                .password("TestTest123!@#")
                .confirmPassword("TestTest123!@#")
                .build();

        ResponseEntity<UserDto> response = testRestTemplate.post(getUrlWithEndpoint("/registration"), null, registrationUserDto, UserDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void registerWithExistingEmailTest() {
        final var registrationUserDto = RegistrationUserDto.builder()
                .username("TEST_USER")
                .email("test1@gmail.com")
                .password("TestTest123!@#")
                .confirmPassword("TestTest123!@#")
                .build();

        ResponseEntity<UserDto> response = testRestTemplate.post(getUrlWithEndpoint("/registration"), null, registrationUserDto, UserDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void registerValidAndActivateTest() {
        final var registrationUserDto = RegistrationUserDto.builder()
                .username("TEST_ANOTHER")
                .email("test2@validmail.com")
                .password("TestTest123!@#")
                .confirmPassword("TestTest123!@#")
                .build();

        ResponseEntity<UserDto> response = testRestTemplate.post(getUrlWithEndpoint("/registration"), null, registrationUserDto, UserDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        final var user = response.getBody();
        assertNotNull(user);

        ConfirmationTokenDto tokenDto = confirmationTokenService.getByUserId(user.id);

        ResponseEntity<Void> activationResponse = testRestTemplate.post(
                getUrlWithEndpoint("/confirm-account?token=%s".formatted(tokenDto.getToken())),
                null,
                null,
                Void.class
        );

        assertEquals(HttpStatus.ACCEPTED, activationResponse.getStatusCode());
    }

    @Test
    public void confirmInvalidTokenTest() {
        ResponseEntity<Void> activationResponse = testRestTemplate.post(
                getUrlWithEndpoint("/confirm-account?token=%s".formatted("INVALID_TOKEN")),
                null,
                null,
                Void.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, activationResponse.getStatusCode());
    }

    @Test
    public void confirmNotExistingTokenTest() {
        ResponseEntity<Void> activationResponse = testRestTemplate.post(
                getUrlWithEndpoint("/confirm-account?token=%s".formatted("8834f0ea-b7e3-4f70-820a-6c0b638b3b87")),
                null,
                null,
                Void.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, activationResponse.getStatusCode());
    }

}
