package com.dataart.secondmonth.testcontainers;

import com.dataart.secondmonth.dto.ChangePasswordDto;
import com.dataart.secondmonth.dto.UpdateUserDto;
import com.dataart.secondmonth.dto.UserDto;
import com.dataart.secondmonth.testcontainers.commons.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTests extends AbstractTest {

    private static final String USERNAME = "test";
    private static final String PASSWORD = "TestTest123!@#";

    @Test
    public void getExistingUserTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var response = testRestTemplate.get(
                getUrlWithEndpoint("/user/%d".formatted(100)),
                jwtDto.getToken(),
                UserDto.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final var user = response.getBody();
        assertNotNull(user);
        assertNotNull(user.id);
    }

    @Test
    public void getNotExistingUserTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var response = testRestTemplate.get(
                getUrlWithEndpoint("/user/%d".formatted(9999)),
                jwtDto.getToken(),
                UserDto.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void changePasswordValidTest() {
        final var username = "tttest";
        final var password = "TestTest123!@#";

        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), username, password).getBody();
        assertNotNull(jwtDto);

        final var newPassword = "NewPassword123!@#";
        final var changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setOldPassword(password);
        changePasswordDto.setPassword(newPassword);
        changePasswordDto.setConfirmPassword(newPassword);

        final var response = testRestTemplate.post(
                getUrlWithEndpoint("/users/change-password"),
                jwtDto.getToken(),
                changePasswordDto,
                Void.class
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

}
