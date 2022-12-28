package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(
            value = "select password from \"user\" where id = :userId",
            nativeQuery = true)
    String getEncryptedPasswordByUserId(@Param("userId") Long userId);

    @Query(
            value = "update \"user\" set password = :newPassword where id = :userId",
            nativeQuery = true)
    @Modifying
    void updatePasswordByUserId(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    Optional<User> findByEmailIgnoreCase(String email);

    @Query(
            value = "update \"user\" set recommended_email_send_at = :dateTime where login = :login",
            nativeQuery = true)
    @Modifying
    void setRecommendationEmailDateTime(@Param("login") String login, @Param("dateTime") LocalDateTime dateTime);

    @Query(
            value = "select * from \"user\" where trunc(date_part('day', current_date - last_logon)/7) >= 1 and " +
                    "(date_part('day', current_date - recommended_email_send_at) = :days or recommended_email_send_at is null)",
            countQuery = "select count(*) from \"user\" where trunc(date_part('day', current_date - last_logon)/7) >= 1 and " +
                    "(date_part('day', current_date - recommended_email_send_at) = :days or recommended_email_send_at is null)",
            nativeQuery = true)
    Page<User> getAllInactiveForRemind(@Param("days") Integer days, Pageable pageable);

}
