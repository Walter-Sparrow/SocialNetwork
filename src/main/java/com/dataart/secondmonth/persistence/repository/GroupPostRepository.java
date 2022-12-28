package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.GroupPost;
import com.dataart.secondmonth.persistence.projection.GroupPostProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {

    @Query(
            value = "with reactionsCountOnPost as ( " +
                    "    select post_id, " +
                    "           sum(case when islike then 1 else 0 end) as likesCount, " +
                    "           sum(case when islike then 0 else 1 end) as dislikesCount " +
                    "    from likes " +
                    "    group by post_id) " +
                    "select gp.*, " +
                    "       l.islike, " +
                    "       coalesce(rp.likesCount, 0)    as likesCount, " +
                    "       coalesce(rp.dislikesCount, 0) as dislikesCount, " +
                    "       u.login as userLogin, " +
                    "       g.name as groupName " +
                    "from group_post as gp " +
                    "         join group_members gm on gp.group_id = gm.group_id " +
                    "         left join likes l on gp.id = l.post_id and l.user_id = :userId " +
                    "         left join reactionsCountOnPost rp on gp.id = rp.post_id " +
                    "         join \"user\" u on u.id = gp.user_id " +
                    "         join \"group\" g on g.id = gp.group_id " +
                    "where gm.user_id = :userId " +
                    "order by gp.created_at desc",
            countQuery = "select count(*) from group_post gp join group_members gm on gp.group_id = gm.group_id where gm.user_id = :userId",
            nativeQuery = true)
    Page<GroupPostProjection> getUserFeedByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = "with reactionsCountOnPost as ( " +
                    "    select post_id, " +
                    "           sum(case when islike then 1 else 0 end) as likesCount, " +
                    "           sum(case when islike then 0 else 1 end) as dislikesCount " +
                    "    from likes " +
                    "    group by post_id) " +
                    "select gp.*, " +
                    "       l.islike, " +
                    "       coalesce(rp.likesCount, 0)    as likesCount, " +
                    "       coalesce(rp.dislikesCount, 0) as dislikesCount, " +
                    "       u.login as userLogin, " +
                    "       g.name as groupName " +
                    "from group_post as gp " +
                    "         left join likes l on gp.id = l.post_id and l.user_id = :userId " +
                    "         left join reactionsCountOnPost rp on gp.id = rp.post_id " +
                    "         join \"user\" u on u.id = gp.user_id " +
                    "         join \"group\" g on g.id = gp.group_id " +
                    "where gp.user_id = :userId " +
                    "order by gp.created_at desc",
            countQuery = "select count(*) from group_post gp where gp.user_id = :userId",
            nativeQuery = true)
    Page<GroupPostProjection> getAllByUserId(Long userId, Pageable pageable);

    @Query(
            value = "with reactionsCountOnPost as ( " +
                    "    select post_id, " +
                    "           sum(case when islike then 1 else 0 end) as likesCount, " +
                    "           sum(case when islike then 0 else 1 end) as dislikesCount " +
                    "    from likes " +
                    "    group by post_id) " +
                    "select gp.*, " +
                    "       l.islike, " +
                    "       coalesce(rp.likesCount, 0)    as likesCount, " +
                    "       coalesce(rp.dislikesCount, 0) as dislikesCount, " +
                    "       u.login as userLogin, " +
                    "       g.name as groupName " +
                    "from group_post gp " +
                    "         left join likes l on gp.id = l.post_id and l.user_id = :authorizedId " +
                    "         left join reactionsCountOnPost rp on gp.id = rp.post_id " +
                    "         join \"group\" g on g.id = gp.group_id " +
                    "         join \"user\" u on u.id = gp.user_id " +
                    "where group_id = :groupId",
            countQuery = "select count(*) from group_post where group_id = :groupId",
            nativeQuery = true)
    Page<GroupPostProjection> getAllByGroupIdWithAuthorizedUserReactions(Long groupId, Long authorizedId, Pageable pageable);

    @Query(
            value = "select gp.*," +
                    "           sum(case when islike then 1 else 0 end) as likesCount, " +
                    "           sum(case when islike then 0 else 1 end) as dislikesCount, " +
                    "       null as islike," +
                    "       u.login as userLogin, " +
                    "       g.name as groupName " +
                    "from group_post gp left join likes l on gp.id = l.post_id " +
                    "         join \"group\" g on g.id = gp.group_id " +
                    "         join \"user\" u on u.id = gp.user_id " +
                    "where " +
                    "      extract(month from gp.created_at) = extract(month from current_date) and " +
                    "      extract(year from gp.created_at) = extract(year from current_date) " +
                    "group by gp.id, gp.created_at, u.login, g.name " +
                    "having sum(case when l.islike then 1 else 0 end) > 0 " +
                    "order by sum(case when l.islike then 1 else 0 end) desc , gp.created_at desc " +
                    "limit 4",
            nativeQuery = true)
    List<GroupPostProjection> getTop4WithHighestLikeCountInThisMonth();

}
