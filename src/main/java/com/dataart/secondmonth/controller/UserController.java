package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.component.DefaultPaginationProperties;
import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.service.GroupPostService;
import com.dataart.secondmonth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GroupPostService groupPostService;

    private final DefaultPaginationProperties paginationProperties;

    @GetMapping("users")
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        PageDto pageDto = paginationProperties.getPageDto();
        pageDto.setPageNumber(pageNumber != null ? pageNumber : paginationProperties.getPageNumber());

        return ResponseEntity.ok(userService.getAll(pageDto));
    }

    @GetMapping("user/{id}")
    public ResponseEntity<UserDto> userDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("user/byUsername/{username}")
    public ResponseEntity<UserDto> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @GetMapping("user/{id}/followings")
    public ResponseEntity<Page<GroupDto>> userFollowings(
            @PathVariable Long id,
            @RequestParam(value = "followingsPageNumber", required = false) Integer followingsPageNumber) {

        PageDto followingsPageDto = paginationProperties.getPageDto();
        followingsPageDto.setPageNumber(followingsPageNumber != null ? followingsPageNumber : paginationProperties.getPageNumber());

        return ResponseEntity.ok(userService.getUserGroups(id, followingsPageDto));
    }

    @GetMapping("user/{id}/posts")
    public ResponseEntity<Page<GroupPostDto>> userPosts(
            @PathVariable Long id,
            @RequestParam(value = "userPostsPageNumber", required = false) Integer userPostsPageNumber) {

        PageDto userPostsPageDto = paginationProperties.getPageDto();
        userPostsPageDto.setPageNumber(userPostsPageNumber != null ? userPostsPageNumber : paginationProperties.getPageNumber());

        return ResponseEntity.ok(groupPostService.fetchAllPostsByUserId(id, userPostsPageDto));
    }

    @GetMapping("user/following/{groupId}")
    public ResponseEntity<Boolean> isUserFollowingGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(userService.isUserFollowingGroup(groupId, userService.getAuthorisedUser().id));
    }

    @PutMapping("users")
    public ResponseEntity<UserDto> update(@Valid @RequestBody UpdateUserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @PostMapping("users/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        userService.updateAuthorizedUserPassword(changePasswordDto);

        return ResponseEntity.accepted().build();
    }

}
