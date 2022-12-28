package com.dataart.secondmonth.service;

import com.dataart.secondmonth.audit.Audit;
import com.dataart.secondmonth.audit.handler.LoginAuditHandler;
import com.dataart.secondmonth.audit.projections.AuthRequestDtoProjection;
import com.dataart.secondmonth.config.security.jwt.JwtProvider;
import com.dataart.secondmonth.dto.*;
import com.dataart.secondmonth.exception.ConfirmationTokenExpiredException;
import com.dataart.secondmonth.exception.ConfirmationTokenNotFound;
import com.dataart.secondmonth.exception.EmailAlreadyInUseException;
import com.dataart.secondmonth.exception.UsernameAlreadyTakenException;
import com.dataart.secondmonth.persistence.entity.User;
import com.dataart.secondmonth.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    @Audit(
            title = "[Login-Audit]",
            message = "User logs in",
            handler = LoginAuditHandler.class,
            logParams = true,
            projections = {AuthRequestDtoProjection.class})
    public JwtResponseDto authenticate(AuthRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return JwtResponseDto.builder()
                .token(jwt)
                .roles(roles)
                .build();
    }

    public UserDto registerNewUser(RegistrationUserDto userDto) {
        userRepository.findByEmailIgnoreCase(userDto.email)
                .ifPresent(s -> {
                    throw new EmailAlreadyInUseException(String.format(
                            "This email address is already being used.\nuserEmail=%s",
                            userDto.email));
                });
        userRepository.findByUsername(userDto.username)
                .ifPresent(s -> {
                    throw new UsernameAlreadyTakenException(String.format(
                            "Username is already taken.\nusername=%s",
                            userDto.username));
                });

        User user = mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.password));
        user.setCreatedAt(ZonedDateTime.now());
        userRepository.save(user);

        ConfirmationTokenDto confirmationTokenDto = ConfirmationTokenDto.builder()
                .user(mapper.map(user, UserDto.class))
                .build();
        confirmationTokenDto = confirmationTokenService.save(confirmationTokenDto);

        emailSenderService.sendActivationAccountEmail(
                userDto.getEmail(),
                confirmationTokenDto.getToken().toString()
        );

        return mapper.map(user, UserDto.class);
    }

    @Transactional(noRollbackFor = ConfirmationTokenExpiredException.class)
    public void activateAccount(String confirmationToken) {
        ConfirmationTokenDto tokenDto = confirmationTokenService.getToken(UUID.fromString(confirmationToken))
                .orElseThrow(() -> new ConfirmationTokenNotFound("Token does not match."));

        if (confirmationTokenService.isTokenExpired(tokenDto)) {
            confirmationTokenService.removeByToken(UUID.fromString(confirmationToken));
            throw new ConfirmationTokenExpiredException(String.format("Token %s is expired.", tokenDto.getToken()));
        }

        User user = userRepository.findByUsername(tokenDto.getUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("There is no such user with the received login"));

        user.setEnabled(true);
        userRepository.save(user);

        confirmationTokenService.removeByToken(tokenDto.getToken());
    }

}
