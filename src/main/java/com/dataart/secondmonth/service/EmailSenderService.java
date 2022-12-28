package com.dataart.secondmonth.service;

import com.dataart.secondmonth.persistence.projection.GroupProjection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailSenderService {

    public static final String CONFIRM_EMAIL_HTML = "mail/confirmEmail.html";
    public static final String BEST_WEEKLY_GROUPS_HTML = "mail/newPoppingGroups.html";
    public static final String BEST_WEEKLY_GROUPS_SUBJECT = "Don't miss it!";
    private static final String FROM_PROPERTY = "VKClone";
    private static final String ACTIVATION_ACCOUNT_SUBJECT = "Complete Registration!";
    private final JavaMailSender mailSender;
    private final TemplateEngine engine;

    @Value("${frontend-address}")
    private String baseUrl;

    @SneakyThrows
    public void sendActivationAccountEmail(String to, String token) {
        Context context = new Context();
        context.setVariable(
                "url",
                baseUrl
                        .concat("/confirm-account?token=")
                        .concat(token)
        );

        sendMimeMessage(to, context, CONFIRM_EMAIL_HTML, ACTIVATION_ACCOUNT_SUBJECT);
    }

    @SneakyThrows
    public void sendWeeklyBestGroups(String to, List<GroupProjection> groups) {
        Context context = new Context();
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("groups", groups);

        sendMimeMessage(to, context, BEST_WEEKLY_GROUPS_HTML, BEST_WEEKLY_GROUPS_SUBJECT);
    }

    @SneakyThrows
    private void sendMimeMessage(String to, Context context, String htmlPath, String subject) {
        String process = engine.process(htmlPath, context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setText(process, true);
        mimeMessageHelper.setFrom(FROM_PROPERTY);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setTo(Objects.requireNonNull(to));

        mailSender.send(mimeMessage);
    }

}
