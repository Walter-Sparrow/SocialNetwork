package com.dataart.secondmonth.service;

import com.dataart.secondmonth.audit.Audit;
import com.dataart.secondmonth.audit.handler.FollowGroupAuditHandler;
import com.dataart.secondmonth.audit.handler.UnfollowGroupAuditHandler;
import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.exception.GroupNameAlreadyInUseException;
import com.dataart.secondmonth.exception.NotFoundException;
import com.dataart.secondmonth.persistence.entity.Group;
import com.dataart.secondmonth.persistence.entity.Image;
import com.dataart.secondmonth.persistence.entity.User;
import com.dataart.secondmonth.persistence.projection.GroupProjection;
import com.dataart.secondmonth.persistence.repository.GroupRepository;
import com.dataart.secondmonth.persistence.repository.UserRepository;
import com.dataart.secondmonth.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final ModelMapper mapper;

    @Value("${search.groups.minLength}")
    private int minLengthSearchQuery;

    public List<GroupDto> getTop4ByMembersCount() {
        return groupRepository.getTop4ByMembersCount().stream()
                .map(group -> mapper.map(group, GroupDto.class))
                .collect(Collectors.toList());
    }

    public Page<GroupDto> searchByName(String name, PageDto pageDto) {
        if (name != null && name.length() < minLengthSearchQuery) {
            throw new IllegalArgumentException("Incorrect search query length.");
        }

        return groupRepository.findByNameContainingIgnoreCase(name, pageDto.getPageable())
                .map(group -> mapper.map(group, GroupDto.class));
    }

    public GroupDto getById(Long id) {
        return mapper.map(
                groupRepository.findById(id).orElseThrow(() ->
                        new NotFoundException("Such a group with the received ID does not exist.")),
                GroupDto.class);
    }

    @Audit(
            title = "[Follow-Group-Audit]",
            message = "User has started to follow the group",
            handler = FollowGroupAuditHandler.class,
            logUserInfo = true)
    public void attachNewMember(Long userId, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new NotFoundException("Such a group with the received ID does not exist."));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("There is no such user with the received ID."));

        user.getGroups().add(group);

        userRepository.save(user);
    }

    @Audit(
            title = "[Unfollow-Group-Audit]",
            message = "User has stopped following the group",
            handler = UnfollowGroupAuditHandler.class,
            logUserInfo = true)
    public void detachMember(Long memberId, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new NotFoundException("Such a group with the received ID does not exist."));
        User user = userRepository.findById(memberId).orElseThrow(() ->
                new NotFoundException("There is no such user with the received ID."));

        user.getGroups().remove(group);

        userRepository.save(user);
    }

    public GroupDto addGroup(GroupCreationDto groupDto) {
        groupRepository.findByName(groupDto.getName())
                .ifPresent(s -> {
                    throw new GroupNameAlreadyInUseException(String.format(
                            "Group name already exists.\nname=%s",
                            groupDto.getName()));
                });

        UserDto user = userService.getAuthorisedUser();

        Group group = mapper.map(groupDto, Group.class);
        group.setOwner(mapper.map(user, User.class));
        group.setCreatedAt(ZonedDateTime.now());
        if (groupDto.getPictureId() != null) {
            group.setGroupPicture(mapper.map(imageService.getById(groupDto.getPictureId()), Image.class));
        }

        groupRepository.save(group);

        attachNewMember(user.id, group.getId());

        return mapper.map(group, GroupDto.class);
    }

    public void deleteById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Such a group with the received ID does not exist."));
        UserDto user = userService.getAuthorisedUser();

        if (!user.id.equals(group.getOwner().getId())) {
            throw new SecurityException(String.format(
                    "Only the owner of a group can delete it.\nuserId=%d\ngroupId=%d",
                    user.id,
                    group.getId()));
        }

        groupRepository.deleteById(id);
    }

    public List<GroupProjection> getBestWeekGroupsInfo() {
        return groupRepository.getTop5ByMembersThisWeek();
    }

    public Page<GroupDto> getAll(PageDto pageDto) {
        return groupRepository.findAll(pageDto.getPageable())
                .map(group -> mapper.map(group, GroupDto.class));
    }

    public GroupDto updateGroup(GroupUpdateDto group) {
        Group groupDB = groupRepository.findById(group.getId())
                .orElseThrow(() -> new NotFoundException("There is no such group with the received ID."));

        if (!groupDB.getName().equals(group.getName())) {
            groupRepository.findByName(group.getName())
                    .ifPresent(s -> {
                        throw new GroupNameAlreadyInUseException(String.format(
                                "Group name already exists.\nname=%s",
                                group.getName()));
                    });
        }

        UserDto user = userService.getAuthorisedUser();

        if (!user.id.equals(groupDB.getOwner().getId())) {
            throw new SecurityException(String.format(
                    "Only the owner of a group can edit it.\nuserId=%d\ngroupId=%d",
                    user.id,
                    group.getId()));
        }

        if (groupDB.getGroupPicture() != null && !Objects.equals(groupDB.getGroupPicture().getId(), group.getPictureId())) {
            imageService.deleteById(groupDB.getGroupPicture().getId());
        }

        if (group.getPictureId() == null) {
            groupDB.setGroupPicture(null);
        } else {
            groupDB.setGroupPicture(mapper.map(imageService.getById(group.getPictureId()), Image.class));
        }

        groupDB.setName(group.getName());
        groupDB.setStatus(group.getStatus());
        groupDB.setDescription(group.getDescription());

        return mapper.map(groupRepository.save(groupDB), GroupDto.class);
    }

}
