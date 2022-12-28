package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.component.DefaultPaginationProperties;
import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.service.GroupPostService;
import com.dataart.secondmonth.service.GroupService;
import com.dataart.secondmonth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupPostService groupPostService;
    private final UserService userService;

    private final DefaultPaginationProperties paginationProperties;

    @GetMapping("groups")
    public ResponseEntity<Page<GroupDto>> viewAll(
            @RequestParam(value = "groupName", required = false) String groupName,
            @RequestParam(value = "groupsPageNumber", required = false) Integer groupsPageNumber) {

        PageDto groupsPageDto = paginationProperties.getPageDto();
        groupsPageDto.setPageNumber(groupsPageNumber != null ? groupsPageNumber : paginationProperties.getPageNumber());
        groupsPageDto.setSortBy("name");

        return ResponseEntity.ok(
                groupName == null || groupName.isBlank() ?
                        groupService.getAll(groupsPageDto) :
                        groupService.searchByName(groupName, groupsPageDto));
    }

    @GetMapping("group/{id}")
    public ResponseEntity<GroupDto> details(@PathVariable("id") Long id) {
        return ResponseEntity.ok(groupService.getById(id));
    }

    @GetMapping("group/{id}/posts")
    public ResponseEntity<Page<GroupPostDto>> groupPosts(
            @PathVariable("id") Long id,
            @RequestParam(value = "postsPageNumber", required = false) Integer postsPageNumber) {

        PageDto postsPage = paginationProperties.getPageDto();
        postsPage.setPageNumber(postsPageNumber != null ? postsPageNumber : paginationProperties.getPageNumber());
        postsPage.setSortBy("created_At");

        return ResponseEntity.ok(groupPostService.getAllByGroupIdWithAuthorizedUserReactions(id, postsPage));
    }

    @PostMapping("group/{id}/join")
    public ResponseEntity<?> join(@PathVariable("id") Long id) {
        UserDto user = userService.getAuthorisedUser();

        groupService.attachNewMember(user.id, id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("group/{id}/leave")
    public ResponseEntity<?> leave(@PathVariable("id") Long id) {
        UserDto user = userService.getAuthorisedUser();

        groupService.detachMember(user.id, id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("groups/best")
    public ResponseEntity<List<GroupDto>> getBestGroups() {
        return ResponseEntity.ok(groupService.getTop4ByMembersCount());
    }

    @GetMapping("feed/groups")
    public ResponseEntity<Page<GroupDto>> getFeedGroups(@RequestParam(value = "feedGroupsPageNumber", required = false) Integer feedPostsPageNumber) {
        PageDto feedPageDto = paginationProperties.getPageDto();
        feedPageDto.setPageNumber(feedPostsPageNumber != null ? feedPostsPageNumber : paginationProperties.getPageNumber());

        return ResponseEntity.ok(userService.getAuthorizedUserGroups(feedPageDto));
    }

    @PostMapping("groups")
    public ResponseEntity<GroupDto> addNewGroup(@Valid @RequestBody GroupCreationDto groupDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.addGroup(groupDto));
    }

    @DeleteMapping("group/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        groupService.deleteById(id);

        return ResponseEntity.accepted().build();
    }

    @PutMapping("groups")
    public ResponseEntity<GroupDto> updateGroup(@Valid @RequestBody GroupUpdateDto group) {
        return ResponseEntity.ok(groupService.updateGroup(group));
    }

}
