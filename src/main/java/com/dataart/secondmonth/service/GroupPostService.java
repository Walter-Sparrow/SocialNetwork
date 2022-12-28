package com.dataart.secondmonth.service;

import com.dataart.secondmonth.audit.Audit;
import com.dataart.secondmonth.audit.handler.ReactionAuditHandler;
import com.dataart.secondmonth.audit.handler.UpdatePostAuditHandler;
import com.dataart.secondmonth.audit.projections.GroupPostUpdateDtoProjection;
import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.exception.EmptyPostException;
import com.dataart.secondmonth.exception.NotFoundException;
import com.dataart.secondmonth.persistence.entity.*;
import com.dataart.secondmonth.persistence.projection.GroupPostProjection;
import com.dataart.secondmonth.persistence.repository.GroupPostRepository;
import com.dataart.secondmonth.persistence.repository.LinkRepository;
import com.dataart.secondmonth.persistence.repository.UserReactionRepository;
import com.dataart.secondmonth.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupPostService {

    private final GroupPostRepository repository;
    private final UserReactionRepository userReactionRepository;
    private final LinkRepository linkRepository;
    private final GroupService groupService;
    private final PostCommentService postCommentService;
    private final UserService userService;
    private final ImageService imageService;
    private final LinkService linkService;
    private final ModelMapper mapper;

    @Value("${post.attachments.maxSize}")
    private Integer attachmentSize;

    public Page<GroupPostDto> getAllByGroupIdWithAuthorizedUserReactions(Long groupId, PageDto pageDto) {
        UserDto user = userService.getAuthorisedUser();
        return repository.getAllByGroupIdWithAuthorizedUserReactions(groupId, user.id, pageDto.getPageable())
                .map(this::fetchDataToGroupPostDto);
    }

    public Page<GroupPostDto> fetchAllPostsByUserId(Long id, PageDto pageDto) {
        return repository.getAllByUserId(id, pageDto.getPageable())
                .map(this::fetchDataToGroupPostDto);
    }

    public List<GroupPostDto> getTop5PostsInThisMonthAndYear() {
        return repository.getTop4WithHighestLikeCountInThisMonth().stream()
                .map(this::fetchDataToGroupPostDto)
                .collect(Collectors.toList());
    }

    public Page<GroupPostDto> getAuthorizedUserFeed(PageDto pageDto) {
        UserDto user = userService.getAuthorisedUser();
        return repository.getUserFeedByUserId(user.id, pageDto.getPageable())
                .map(this::fetchDataToGroupPostDto);
    }

    public GroupPostDto getById(Long id) {
        return mapper.map(
                repository.findById(id).orElseThrow(() ->
                        new NotFoundException("Such a group post with the received ID does not exist.")),
                GroupPostDto.class);
    }

    public GroupPostDto addPost(Long groupId, GroupPostCreationDto post) {
        if (post.getImageIds() != null && post.getImageIds().size() > attachmentSize) {
            throw new IllegalArgumentException("The number of attachments in the message is exceeded.");
        }

        final GroupDto group = groupService.getById(groupId);
        final UserDto user = userService.getAuthorisedUser();

        if (!userService.isUserFollowingGroup(group.getId(), user.getId())) {
            throw new SecurityException(String.format(
                    "Only group members can add new posts.\nuserId=%d\ngroupId=%d",
                    user.getId(),
                    group.getId()));
        }

        List<Image> attachmentImages = post.getImageIds() == null ? null : post.getImageIds().stream()
                .map(id -> mapper.map(imageService.getById(id), Image.class))
                .collect(Collectors.toList());

        Link link = null;
        if (post.getLink() != null) {
            link = linkRepository
                    .findByUrl(post.getLink().getUrl())
                    .orElse(mapper.map(post.getLink(), Link.class));

            if (link.getId() == null) {
                linkRepository.save(link);
            }
        }

        GroupPost groupPost = GroupPost.builder()
                .group(mapper.map(group, Group.class))
                .user(mapper.map(user, User.class))
                .text(post.getText())
                .images(attachmentImages)
                .linkAttachment(link)
                .createdAt(ZonedDateTime.now())
                .build();

        return mapper.map(repository.save(groupPost), GroupPostDto.class);
    }

    @Audit(
            title = "[Update-Post-Audit]",
            message = "User updated post",
            handler = UpdatePostAuditHandler.class,
            logParams = true,
            projections = {GroupPostUpdateDtoProjection.class},
            logUserInfo = true)
    public GroupPostDto updatePost(GroupPostUpdateDto post) {
        if (post.getImageIds() != null && post.getImageIds().size() > attachmentSize) {
            throw new IllegalArgumentException("The number of attachments in the message is exceeded.");
        }

        GroupPost groupPostDB = repository
                .findById(post.getId())
                .orElseThrow(() ->
                        new NotFoundException("Such a group post with the received ID does not exist."));

        if (post.getText() == null && groupPostDB.getImages().size() == 0 && post.getImageIds() == null) {
            throw new EmptyPostException("The post body cannot be empty.");
        }

        UserDto user = userService.getAuthorisedUser();

        Link link = null;
        if (post.getLink() != null) {
            link = linkRepository
                    .findByUrl(post.getLink().getUrl())
                    .orElse(mapper.map(post.getLink(), Link.class));

            if (link.getId() == null) {
                linkRepository.save(link);
            }
        }

        if (!groupPostDB.getUser().getId().equals(user.id) && !groupPostDB.getGroup().getOwner().getId().equals(user.id)) {
            throw new SecurityException(String.format(
                    "Only the author of a post or the owner of a group can modify posts.\nuserId=%d\ngroupId=%d",
                    user.id,
                    groupPostDB.getGroup().getId()));
        }

        if (post.getImageIds() == null || post.getImageIds().size() <= 0) {
            groupPostDB.setImages(null);
        } else {
            groupPostDB.setImages(
                    post.getImageIds().stream()
                            .map(id -> Image.builder().id(id).build())
                            .collect(Collectors.toList())
            );
        }

        groupPostDB.setText(post.getText());
        groupPostDB.setLinkAttachment(link);

        GroupPost updated = repository.save(groupPostDB);
        return mapper.map(updated, GroupPostDto.class);
    }

    public void deletePost(Long postId) {
        UserDto user = userService.getAuthorisedUser();
        GroupPostDto post = getById(postId);

        if (!post.getUser().id.equals(user.id) && !post.getGroup().getOwner().id.equals(user.id)) {
            throw new SecurityException(String.format(
                    "Only the author of a post or the owner of a group can delete posts.\nuserId=%d",
                    user.id));
        }

        post.getImages().forEach(image -> imageService.deleteById(image.getId()));

        repository.deleteById(postId);
    }

    @Audit(
            title = "[Reaction-Audit]",
            message = "reaction",
            handler = ReactionAuditHandler.class,
            logParams = true,
            logUserInfo = true)
    public void reactOnPost(UserReactionDto userReaction) {
        UserDto user = userService.getAuthorisedUser();
        GroupPostDto post = getById(userReaction.getPostId());

        if (!user.id.equals(userReaction.getUserId())) {
            throw new SecurityException(String.format(
                    "User ID mismatch. The authorized user ID does not match the user ID from the dto.\nauthUserId=%d, userId from dto=%d ",
                    user.id,
                    userReaction.getUserId()));
        }

        Optional<UserReactionDto> reactionDB = userReactionRepository.getUserReactionByUserIdAndPostId(user.getId(), post.getId())
                .map(reaction -> mapper.map(reaction, UserReactionDto.class));

        if (reactionDB.isPresent() && reactionDB.get().isLike() == userReaction.isLike()) {
            removeReactionFromPost(reactionDB.get());
        } else {
            reactionDB.ifPresent(userReactionDto -> userReaction.setId(userReactionDto.getId()));
            userReaction.setUser(user);
            userReaction.setPost(post);
            addReactionOnPost(userReaction);
        }
    }

    private void addReactionOnPost(UserReactionDto userReactionDto) {
        mapper.map(
                userReactionRepository.save(mapper.map(userReactionDto, UserReaction.class)),
                UserReactionDto.class);
    }

    private void removeReactionFromPost(UserReactionDto userReactionDto) {
        userReactionRepository.delete(mapper.map(userReactionDto, UserReaction.class));
    }

    private GroupPostDto fetchDataToGroupPostDto(GroupPostProjection postProjection) {
        GroupPostDto postDto = mapper.map(postProjection, GroupPostDto.class);

        postDto.setUser(userService.getById(postProjection.getUserId()));
        postDto.setGroup(groupService.getById(postProjection.getGroupId()));
        postDto.setImages(imageService.getPostImagesByPostId(postProjection.getId()));
        postDto.setComments(postCommentService.getAllTopLayerByPostId(postProjection.getId()));
        postDto.setLinkAttachment(linkService.getRichLinkByPostId(postProjection.getId()));

        return postDto;
    }

    public UserReactionDto getAuthUserReactionByPostId(Long id) {
        UserDto user = userService.getAuthorisedUser();
        return mapper.map(
                userReactionRepository.getUserReactionByUserIdAndPostId(user.id, id)
                        .orElseThrow(() -> new NotFoundException("Reaction not found.")),
                UserReactionDto.class);
    }

    public List<PostCommentDto> getPostCommentsById(Long id) {
        return postCommentService.getAllTopLayerByPostId(id).stream()
                .map(comment -> mapper.map(comment, PostCommentDto.class))
                .toList();
    }

}
