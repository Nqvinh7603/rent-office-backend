package com.nqvinh.mail.consumer.infrastructure.service;

import com.nqvinh.mail.consumer.domain.event.MailEvent;
import com.nqvinh.mail.consumer.domain.service.MailEngine;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailEngineImpl implements MailEngine {

    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;
    String fromAddress = "cyberreal@gmail.com";
    String senderName = "CYBER REAL";

    @Override
    @SneakyThrows
    @Async
    public void sendMail(MailEvent mail) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(mail.getContext());
        String content = templateEngine.process( mail.getTemplateName(), thymeleafContext);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(mail.getToAddress());
        helper.setSubject(mail.getSubject());
        helper.setText(content, true);

        mailSender.send(message);
    }

}



