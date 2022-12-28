package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.Group;
import com.dataart.secondmonth.persistence.projection.GroupProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Page<Group> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(
            value = "select g.* from \"group\" g join group_members gm on g.id = gm.group_id where gm.user_id = :userId",
            countQuery = "select count(*) from group_members where user_id = :userId",
            nativeQuery = true)
    Page<Group> getAllUsersGroupsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = "select count(*) > 0 from group_members as gm where user_id = :userId and group_id = :groupId",
            nativeQuery = true)
    boolean isUserFollowingGroup(@Param("userId") Long userId, @Param("groupId") Long groupId);

    @Query(
            value = "select g.* " +
                    "from \"group\" g " +
                    "         left join group_members gm on g.id = gm.group_id " +
                    "group by g.id " +
                    "order by count(gm.user_id) desc " +
                    "limit 4",
            nativeQuery = true)
    List<Group> getTop4ByMembersCount();

    @Query(
            value = "select g.id, g.name, count(gm.user_id) as membersCount " +
                    "from \"group\" g " +
                    "         left join group_members gm on g.id = gm.group_id " +
                    "where trunc(date_part('day', current_date - g.created_at)/7) <= 1 " +
                    "group by g.id " +
                    "order by count(gm.user_id) desc " +
                    "limit 5",
            nativeQuery = true)
    List<GroupProjection> getTop5ByMembersThisWeek();

    Page<Group> findAll(Pageable pageable);

    Optional<Group> findByName(String name);

}
