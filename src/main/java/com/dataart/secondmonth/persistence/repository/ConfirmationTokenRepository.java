package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.ConfirmationToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {

    @Query(
            value = "with expired_tokens as ( " +
                    "   select distinct id " +
                    "   from confirmation_token " +
                    "   where created_at + interval '1 second'*:expirationTimeSeconds <= current_timestamp " +
                    ") " +
                    "delete from confirmation_token " +
                    "where id in (select * from expired_tokens)",
            nativeQuery = true
    )
    @Modifying
    void removeExpiredTokens(@Param("expirationTimeSeconds") Integer expirationTimeSeconds);

    ConfirmationToken getByUserId(Long userId);

}
