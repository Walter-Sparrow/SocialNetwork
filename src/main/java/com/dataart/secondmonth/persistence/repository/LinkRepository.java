package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByUrl(String url);

    @Query(
            value = "select link.* from link join group_post gp on link.id = gp.link_attachment_id where gp.id = :postId",
            nativeQuery = true)
    Link getRichLinkByPostId(Long postId);

    @Query(
            value = "with unusedLinkIds as ( " +
                    "    select distinct link.id " +
                    "    from link " +
                    "             left join group_post gp on link.id = gp.link_attachment_id " +
                    "    where gp.link_attachment_id is null " +
                    ") " +
                    "delete from link " +
                    "where id in (select id from unusedLinkIds)",
            nativeQuery = true)
    @Modifying
    void removeAllUnusedLinks();

}
