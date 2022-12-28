package com.dataart.secondmonth.job;

import com.dataart.secondmonth.component.DefaultPaginationProperties;
import com.dataart.secondmonth.dto.PageDto;
import com.dataart.secondmonth.dto.UserDto;
import com.dataart.secondmonth.persistence.projection.GroupProjection;
import com.dataart.secondmonth.service.EmailSenderService;
import com.dataart.secondmonth.service.GroupService;
import com.dataart.secondmonth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailReminderJob {

    private final GroupService groupService;
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final DefaultPaginationProperties paginationProperties;

    @Scheduled(cron = "${jobs.email-reminder.cron}")
    @Transactional
    public void remindUsersInactiveWeek() {
        List<GroupProjection> groups = groupService.getBestWeekGroupsInfo();

        if (groups.size() == 0) {
            log.info("No groups found for recommendation.");
            return;
        }
        PageDto usersPage = paginationProperties.getPageDto();
        Page<UserDto> users = userService.getAllInactiveForRemind(usersPage);

        while (!users.isEmpty()) {
            usersPage.setPageNumber(usersPage.getPageNumber() + 1);

            users.forEach(user -> {
                emailSenderService.sendWeeklyBestGroups(user.email, groups);
                userService.setRecommendationEmailDateTime(user.username, LocalDateTime.now());
            });

            users = userService.getAllInactiveForRemind(usersPage);
        }
    }

}
