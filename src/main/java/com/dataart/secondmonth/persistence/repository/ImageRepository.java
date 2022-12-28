package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(
            value = "select i.* from attachment a join image i on i.id = a.image_id where post_id = :postId",
            nativeQuery = true)
    List<Image> getPostImagesByPostId(Long postId);

}
