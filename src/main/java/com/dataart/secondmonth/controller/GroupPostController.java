package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.component.DefaultPaginationProperties;
import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.service.GroupPostService;
import com.dataart.secondmonth.service.PostCommentService;
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
public class GroupPostController {

    private final GroupPostService groupPostService;
    private final PostCommentService postCommentService;

    private final DefaultPaginationProperties paginationProperties;

    @PostMapping(value = "group/{id}/posts")
    public ResponseEntity<GroupPostDto> addNewPost(
            @PathVariable Long id,
            @Valid @RequestBody GroupPostCreationDto post) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupPostService.addPost(id, post));
    }

    @DeleteMapping("group/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        groupPostService.deletePost(postId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("group/posts")
    public ResponseEntity<GroupPostDto> updatePost(@Valid @RequestBody GroupPostUpdateDto post) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(groupPostService.updatePost(post));
    }

    @GetMapping("posts/best")
    public ResponseEntity<List<GroupPostDto>> getBestPostsOnTheMonth() {
        return ResponseEntity.ok(groupPostService.getTop5PostsInThisMonthAndYear());
    }

    @GetMapping("feed/posts")
    public ResponseEntity<Page<GroupPostDto>> getFeedPosts(
            @RequestParam(value = "feedPostsPageNumber", required = false) Integer feedPostsPageNumber) {
        PageDto feedPage = paginationProperties.getPageDto();

        feedPage.setPageNumber(feedPostsPageNumber != null ? feedPostsPageNumber : paginationProperties.getPageNumber());
        return ResponseEntity.ok(groupPostService.getAuthorizedUserFeed(feedPage));
    }

    @GetMapping("post/{id}/reaction")
    public ResponseEntity<UserReactionDto> getPostAuthUserReaction(@PathVariable Long id) {
        return ResponseEntity.ok(groupPostService.getAuthUserReactionByPostId(id));
    }

    @PostMapping("post/react")
    public ResponseEntity<?> reactOnPost(@RequestBody UserReactionDto userReaction) {
        groupPostService.reactOnPost(userReaction);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("post/{id}/comments")
    public ResponseEntity<List<PostCommentDto>> getPostComments(@PathVariable Long id) {
        return ResponseEntity.ok(groupPostService.getPostCommentsById(id));
    }

}
