package com.dataart.secondmonth.service;

import com.dataart.secondmonth.persistence.entity.User;
import com.dataart.secondmonth.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuditService {

    private final UserRepository userRepository;

    public void checkinUser(String login) {
        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new UsernameNotFoundException("There is no such user with the received login."));
        user.setLastLogon(LocalDateTime.now());
        userRepository.save(user);
    }

}
