package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReactionRepository extends JpaRepository<UserReaction, Long> {

    Optional<UserReaction> getUserReactionByUserIdAndPostId(Long userId, Long postId);

}
