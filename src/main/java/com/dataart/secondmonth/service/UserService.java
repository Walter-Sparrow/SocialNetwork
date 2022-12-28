package com.dataart.secondmonth.service;

import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.exception.NotFoundException;
import com.dataart.secondmonth.exception.OldPasswordIsIncorrectException;
import com.dataart.secondmonth.exception.OldPasswordSameAsNewException;
import com.dataart.secondmonth.persistence.entity.Image;
import com.dataart.secondmonth.persistence.entity.User;
import com.dataart.secondmonth.persistence.repository.GroupRepository;
import com.dataart.secondmonth.persistence.repository.UserRepository;
import com.dataart.secondmonth.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Value("${scheduling.days-delay}")
    private Integer remindDaysDelay;

    public UserDto getAuthorisedUser() {
        User userDetails = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return mapper.map(
                userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
                        new UsernameNotFoundException("There is no such user with the received login.")),
                UserDto.class);
    }

    public Boolean isUserFollowingGroup(Long groupId, Long userId) {
        return groupRepository.isUserFollowingGroup(userId, groupId);
    }

    public Page<GroupDto> getUserGroups(Long userId, PageDto pageDto) {
        return groupRepository.getAllUsersGroupsByUserId(userId, pageDto.getPageable())
                .map(group -> mapper.map(group, GroupDto.class));
    }

    public Page<GroupDto> getAuthorizedUserGroups(PageDto pageDto) {
        return getUserGroups(getAuthorisedUser().id, pageDto);
    }

    public String getAuthorizedUserEncryptedPassword() {
        return userRepository.getEncryptedPasswordByUserId(getAuthorisedUser().id);
    }

    public UserDto getById(Long userId) {
        return mapper.map(
                userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("There is no such user with the received ID.")),
                UserDto.class);
    }

    public UserDto updateUser(UpdateUserDto user) {
        User userDB = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("There is no such user with the received ID."));

        if (!user.id.equals(getAuthorisedUser().getId())) {
            throw new SecurityException(String.format(
                    "Only the owner of an account can edit it.\nuserId=%d",
                    user.id));
        }

        if (userDB.getProfilePicture() != null && !Objects.equals(userDB.getProfilePicture().getId(), user.getPictureId())) {
            imageService.deleteById(userDB.getProfilePicture().getId());
        }

        if (user.getPictureId() == null) {
            userDB.setProfilePicture(null);
        } else {
            userDB.setProfilePicture(mapper.map(imageService.getById(user.getPictureId()), Image.class));
        }

        userDB.setFirstName(user.getFirstName());
        userDB.setMiddleName(user.getMiddleName());
        userDB.setLastName(user.getLastName());
        userDB.setBirthday(user.getBirthday());

        return mapper.map(userRepository.save(userDB), UserDto.class);
    }

    public void updateAuthorizedUserPassword(ChangePasswordDto changePasswordDto) {
        UserDto user = getAuthorisedUser();

        if (!changePasswordDto.getUserId().equals(user.id)) {
            throw new SecurityException(String.format(
                    "Only account owner can change the password.\nuserId=%d",
                    user.id));
        }

        if (!passwordEncoder.matches(
                changePasswordDto.getOldPassword(),
                getAuthorizedUserEncryptedPassword())) {
            throw new OldPasswordIsIncorrectException(String.format(
                    "The old password is incorrect.\nuserId=%d",
                    user.id));
        }

        if (passwordEncoder.matches(
                changePasswordDto.getPassword(),
                getAuthorizedUserEncryptedPassword())) {
            throw new OldPasswordSameAsNewException(String.format(
                    "The old password is the same as the new one.\nuserId=%d",
                    user.id));
        }

        userRepository.updatePasswordByUserId(
                user.id,
                passwordEncoder.encode(changePasswordDto.password));
    }

    public Page<UserDto> getAllInactiveForRemind(PageDto pageDto) {
        return userRepository.getAllInactiveForRemind(remindDaysDelay, pageDto.getPageable())
                .map(user -> mapper.map(user, UserDto.class));
    }

    public void setRecommendationEmailDateTime(String login, LocalDateTime dateTime) {
        userRepository.setRecommendationEmailDateTime(login, dateTime);
    }

    public Page<UserDto> getAll(PageDto pageDto) {
        return userRepository.findAll(pageDto.getPageable()).map(user -> mapper.map(user, UserDto.class));
    }

    public UserDto getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(u -> mapper.map(u, UserDto.class))
                .orElse(null);
    }

}
