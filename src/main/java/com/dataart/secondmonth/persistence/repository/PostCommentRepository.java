package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.PostComment;
import com.dataart.secondmonth.persistence.projection.PostCommentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    @Query(
            value = "select *, " +
                    "   (select count(*) from post_comment where parent_id=p.id) as repliesCount, " +
                    "   (select id from post_comment where parent_id=p.id order by createdat limit 1) as first_reply " +
                    "from post_comment as p " +
                    "where p.post_id=:postId and p.parent_id is null",
            nativeQuery = true)
    List<PostCommentProjection> getAllTopLayerByPostId(Long postId);

    List<PostComment> getAllByParentId(Long parentId);

    List<PostComment> getAllByPostIdOrderById(Long postId);

}
