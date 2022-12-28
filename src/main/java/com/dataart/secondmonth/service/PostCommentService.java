package com.dataart.secondmonth.service;

import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.exception.EmptyPostCommentException;
import com.dataart.secondmonth.exception.NotFoundException;
import com.dataart.secondmonth.persistence.entity.GroupPost;
import com.dataart.secondmonth.persistence.entity.PostComment;
import com.dataart.secondmonth.persistence.entity.User;
import com.dataart.secondmonth.persistence.repository.GroupPostRepository;
import com.dataart.secondmonth.persistence.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    private final UserService userService;
    private final GroupPostRepository groupPostRepository;

    private final ModelMapper mapper;

    public List<TopLayerPostCommentDto> getAllTopLayerByPostId(Long postId) {
        return postCommentRepository.getAllTopLayerByPostId(postId).stream()
                .map(comment -> TopLayerPostCommentDto.builder()
                        .id(comment.getId())
                        .text(comment.getText())
                        .createdAt(comment.getCreatedAt())
                        .repliesCount(comment.getRepliesCount())
                        .firstReply(
                                comment.getFirstReplyId() == null ? null :
                                        postCommentRepository
                                                .findById(comment.getFirstReplyId())
                                                .map(c -> mapper.map(c, PostCommentDto.class))
                                                .orElse(null)
                        )
                        .post(mapper.map(groupPostRepository.getById(comment.getPostId()), GroupPostDto.class))
                        .user(userService.getById(comment.getUserId()))
                        .build())
                .collect(Collectors.toList());
    }

    public TopLayerPostCommentDto leaveComment(LeavePostCommentDto postCommentDto) {
        PostComment comment = extractCommentFromDto(postCommentDto);

        return mapper.map(postCommentRepository.save(comment), TopLayerPostCommentDto.class);
    }

    public PostCommentDto replyToComment(LeavePostCommentDto commentDto) {
        PostComment comment = extractCommentFromDto(commentDto);

        if (commentDto.getReplyId() != null) {
            PostComment reply = postCommentRepository.findById(commentDto.getReplyId())
                    .orElseThrow(() -> new NotFoundException("The comment with the given id was not found."));
            comment.setReply(reply);
        }

        return mapper.map(postCommentRepository.save(comment), PostCommentDto.class);
    }

    private PostComment extractCommentFromDto(LeavePostCommentDto commentDto) {
        if (commentDto.getText().isBlank()) {
            throw new EmptyPostCommentException("The post comment body cannot be empty.");
        }

        UserDto user = userService.getAuthorisedUser();
        GroupPostDto post = mapper.map(groupPostRepository.getById(commentDto.getPostId()), GroupPostDto.class);

        if (!user.id.equals(commentDto.getUserId())) {
            throw new SecurityException(
                    String.format(
                            "User ID mismatch. The authorized user ID does not match the user ID from the dto.\nauthUserId=%d, userId from dto=%d ",
                            user.id,
                            commentDto.getUserId())
            );
        }

        return PostComment.builder()
                .user(mapper.map(user, User.class))
                .post(mapper.map(post, GroupPost.class))
                .text(commentDto.getText())
                .createdAt(ZonedDateTime.now())
                .build();
    }

    public void deleteComment(Long commentId) {
        UserDto user = userService.getAuthorisedUser();
        PostComment comment = postCommentRepository.getById(commentId);

        if (!comment.getUser().getId().equals(user.id) && !comment.getPost().getGroup().getOwner().getId().equals(user.id)) {
            throw new SecurityException(
                    String.format(
                            "Only the author of a comment or the owner of a group can delete it.\nuserId=%d",
                            user.id)
            );
        }

        postCommentRepository.delete(comment);
    }

    public List<PostCommentDto> getAllRepliesOfCommentWithId(Long commentId) {
        return postCommentRepository.getAllByParentId(commentId).stream()
                .map(comment -> mapper.map(comment, PostCommentDto.class))
                .collect(Collectors.toList());
    }

}
