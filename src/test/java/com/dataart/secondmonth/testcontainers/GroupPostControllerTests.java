package com.dataart.secondmonth.testcontainers;

import com.dataart.secondmonth.dto.GroupPostCreationDto;
import com.dataart.secondmonth.dto.GroupPostDto;
import com.dataart.secondmonth.dto.GroupPostUpdateDto;
import com.dataart.secondmonth.testcontainers.commons.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupPostControllerTests extends AbstractTest {

    private static final String USERNAME = "test";
    private static final String PASSWORD = "TestTest123!@#";

    @Test
    public void suggestValidPostTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var postText = "TEST";
        final var creationPostDto = new GroupPostCreationDto();
        creationPostDto.setText(postText);

        final var response = testRestTemplate.post(
                getUrlWithEndpoint("/group/%d/posts".formatted(100)),
                jwtDto.getToken(),
                creationPostDto,
                GroupPostDto.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        final var post = response.getBody();
        assertNotNull(post);
        assertEquals(postText, post.getText());
    }

    @Test
    public void deleteExistingPostTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var response = testRestTemplate.delete(
                getUrlWithEndpoint("/group/posts/%d".formatted(101)),
                jwtDto.getToken(),
                Void.class
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void updateExistingPostTest() {
        final var jwtDto = testRestTemplate.login(getUrlWithEndpoint("/auth"), USERNAME, PASSWORD).getBody();
        assertNotNull(jwtDto);

        final var postText = "TEST";
        final var postUpdateDto = new GroupPostUpdateDto();
        postUpdateDto.setText(postText);
        postUpdateDto.setId(100L);

        final var response = testRestTemplate.put(
                getUrlWithEndpoint("/group/posts"),
                jwtDto.getToken(),
                postUpdateDto,
                GroupPostDto.class
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        final var updatedPost = response.getBody();
        assertNotNull(updatedPost);
        assertEquals(postText, updatedPost.getText());
    }

}
