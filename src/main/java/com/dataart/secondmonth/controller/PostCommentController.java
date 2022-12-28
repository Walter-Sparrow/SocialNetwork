package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.dto.LeavePostCommentDto;
import com.dataart.secondmonth.dto.PostCommentDto;
import com.dataart.secondmonth.dto.TopLayerPostCommentDto;
import com.dataart.secondmonth.persistence.repository.PostCommentRepository;
import com.dataart.secondmonth.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("comments")
    public ResponseEntity<TopLayerPostCommentDto> leaveComment(@RequestBody LeavePostCommentDto postComment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postCommentService.leaveComment(postComment));
    }

    @PostMapping("comments/reply")
    public ResponseEntity<PostCommentDto> replyToComment(@RequestBody LeavePostCommentDto postCommentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postCommentService.replyToComment(postCommentDto));
    }

    @DeleteMapping("comments/{id}")
    public ResponseEntity<?> removeComment(@PathVariable Long id) {
        postCommentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("comments/{id}/replies")
    public ResponseEntity<List<PostCommentDto>> getAllReplies(@PathVariable Long id) {
        return ResponseEntity.ok(postCommentService.getAllRepliesOfCommentWithId(id));
    }

}
