package com.dataart.secondmonth.service;

import com.dataart.secondmonth.dto.ConfirmationTokenDto;
import com.dataart.secondmonth.persistence.entity.ConfirmationToken;
import com.dataart.secondmonth.persistence.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository repository;
    private final ModelMapper mapper;

    @Value("${jobs.expired-confirmation-tokes-crawler.expiration-time-seconds}")
    private Integer expirationTimeSeconds;

    public Optional<ConfirmationTokenDto> getToken(UUID token) {
        return repository.findById(token).map(t -> mapper.map(t, ConfirmationTokenDto.class));
    }

    public boolean isTokenExpired(ConfirmationTokenDto confirmationTokenDto) {
        ZonedDateTime expirationDate = confirmationTokenDto.getCreatedAt().plus(expirationTimeSeconds, ChronoUnit.SECONDS);
        return expirationDate.isBefore(ZonedDateTime.now());
    }

    public Long getCount() {
        return repository.count();
    }

    public void removeByToken(UUID token) {
        repository.deleteById(token);
    }

    public void removeExpiredTokens() {
        repository.removeExpiredTokens(expirationTimeSeconds);
    }

    public ConfirmationTokenDto save(ConfirmationTokenDto confirmationTokenDto) {
        return mapper.map(
                repository.save(mapper.map(confirmationTokenDto, ConfirmationToken.class)),
                ConfirmationTokenDto.class
        );
    }

    public ConfirmationTokenDto getByUserId(Long userId) {
        return mapper.map(
                repository.getByUserId(userId),
                ConfirmationTokenDto.class
        );
    }
}
