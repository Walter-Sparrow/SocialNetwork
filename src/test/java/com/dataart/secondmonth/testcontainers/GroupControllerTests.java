package com.dataart.secondmonth.testcontainers;

import com.dataart.secondmonth.dto.GroupCreationDto;
import com.dataart.secondmonth.dto.GroupDto;
import com.dataart.secondmonth.dto.GroupUpdateDto;
import com.dataart.secondmonth.testcontainers.commons.AbstractTest;
import com.dataart.secondmonth.testcontainers.commons.ResponsePage;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class GroupControllerTests extends AbstractTest {

    private static final String USERNAME = "test";
    private static final String PASSWORD = "TestTest123!@#";

    @Test
    public void getAllTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var response = testRestTemplate.get(
                getUrlWithEndpoint("/groups"),
                jwtDto.getToken(),
                new ParameterizedTypeReference<ResponsePage<GroupDto>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final var page = response.getBody();
        assertNotNull(page);
        assertTrue(page.getTotalElements() > 0);
    }

    @Test
    public void getExistingGroupDetailsTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var groupId = 100;
        final var response = testRestTemplate.get(
                getUrlWithEndpoint("/group/%d".formatted(groupId)),
                jwtDto.getToken(),
                GroupDto.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final var group = response.getBody();
        assertNotNull(group);
        assertEquals(groupId, group.getId());
    }

    @Test
    public void createValidGroupTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var creationGroupDto = new GroupCreationDto();
        creationGroupDto.setName("TestTestTest");

        final var response = testRestTemplate.post(
                getUrlWithEndpoint("/groups"),
                jwtDto.getToken(),
                creationGroupDto,
                GroupDto.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        final var group = response.getBody();
        assertNotNull(group);
        assertEquals(creationGroupDto.getName(), group.getName());
    }

    @Test
    public void deleteExistingGroupWithRightsTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var response = testRestTemplate.delete(
                getUrlWithEndpoint("/group/%d".formatted(103)),
                jwtDto.getToken(),
                Void.class
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void updateValidExistingGroupTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var groupId = 101L;
        final var updateGroupDto = new GroupUpdateDto();
        updateGroupDto.setId(groupId);
        updateGroupDto.setName("testTesttestTest");

        final var response = testRestTemplate.put(
                getUrlWithEndpoint("/groups"),
                jwtDto.getToken(),
                updateGroupDto,
                GroupDto.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        final var group = response.getBody();
        assertNotNull(group);
        assertEquals(updateGroupDto.getName(), group.getName());
    }

}
