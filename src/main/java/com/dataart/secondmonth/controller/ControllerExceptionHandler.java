package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.dto.ApiErrorDto;
import com.dataart.secondmonth.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> errorHandler(RuntimeException exception) {
        log.error("An unresolved exception occurred.", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(EmptyPostException.class)
    public ResponseEntity<?> emptyPostExceptionHandler(EmptyPostException exception) {
        log.error("The post body cannot be empty.", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentExceptionHandler(IllegalArgumentException exception) {
        log.error("Bad request.", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({BindException.class, TransactionSystemException.class})
    public ResponseEntity<?> bindExceptionHandler(RuntimeException exception) {
        log.error("Bind exception.", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(PasswordMatchException.class)
    public ResponseEntity<?> passwordMatchExceptionHandler(PasswordMatchException exception) {
        log.error("Password and confirm password does not match.", exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> securityExceptionHandler(SecurityException exception) {
        log.error("Security exception.", exception);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler({NotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<?> notFoundExceptionHandler(RuntimeException exception) {
        log.error("Not found.", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException exception) {
        log.error("Max upload size of the file exceeded.", exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(BadCredentialsException exception) {
        log.error("Bad credentials.", exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ApiErrorDto> usernameAlreadyTakenExceptionHandler(UsernameAlreadyTakenException exception) {
        log.error("Username is already taken", exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorDto.builder()
                        .status(HttpStatus.CONFLICT)
                        .message("Username is already taken")
                        .build()
        );
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiErrorDto> emailAlreadyInUseExceptionHandler(EmailAlreadyInUseException exception) {
        log.error("Email already in use", exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorDto.builder()
                        .status(HttpStatus.CONFLICT)
                        .message("Email already in use")
                        .build()
        );
    }

    @ExceptionHandler(GroupNameAlreadyInUseException.class)
    public ResponseEntity<ApiErrorDto> groupNameAlreadyInUseExceptionHandler(GroupNameAlreadyInUseException exception) {
        log.error("Group name already in use", exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorDto.builder()
                        .status(HttpStatus.CONFLICT)
                        .message("Group name already in use")
                        .build()
        );
    }

    @ExceptionHandler(OldPasswordIsIncorrectException.class)
    public ResponseEntity<ApiErrorDto> oldPasswordIsIncorrectExceptionHandler(OldPasswordIsIncorrectException exception) {
        log.error("Old password not accepted", exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiErrorDto.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("Something unexpected happened. Please try again")
                        .build()
        );
    }

    @ExceptionHandler(OldPasswordSameAsNewException.class)
    public ResponseEntity<ApiErrorDto> oldPasswordSameAsNewExceptionHandler(OldPasswordSameAsNewException exception) {
        log.error("The new password cannot be the same as the current password", exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiErrorDto.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("The new password cannot be the same as the current password")
                        .build()
        );
    }

    @ExceptionHandler(ConfirmationTokenExpiredException.class)
    public ResponseEntity<ApiErrorDto> confirmationTokenExpiredExceptionHandler(ConfirmationTokenExpiredException exception) {
        log.error("The token is expired", exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiErrorDto.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("The token is expired. Please contact administrator for more information")
                        .build()
        );
    }

    @ExceptionHandler(ConfirmationTokenNotFound.class)
    public ResponseEntity<ApiErrorDto> confirmationTokenNotFoundHandler(ConfirmationTokenNotFound exception) {
        log.error("Token not found", exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiErrorDto.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("Check if the url is the same as the one from the email")
                        .build()
        );
    }

}
