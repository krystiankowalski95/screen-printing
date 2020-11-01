package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import pl.lodz.it.sitodruk.config.MailSenderConfig;
import pl.lodz.it.sitodruk.service.EmailSenderService;

import javax.servlet.http.HttpServletRequest;

@Component
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private MailSenderConfig emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreplay@mail.com");
        message.setReplyTo("noreplay@mail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.getJavaMailSender().send(message);
    }

    @Override
    public void sendRegistrationEmail(String to , HttpServletRequest request,String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreplay@mail.com");
        message.setReplyTo("noreplay@mail.com");
        message.setTo(to);
        message.setSubject("Welcome");
        String link = request.getRequestURL()
                .substring(0, (request.getRequestURL().length() - request.getServletPath().length())).concat("/cofirmAccount?token=");
        String body = "<a href=\"" + link + token + "\">"+ "</a>";
        message.setText(body);
        emailSender.getJavaMailSender().send(message);
    }
}