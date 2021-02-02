package pl.lodz.it.sitodruk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import pl.lodz.it.sitodruk.config.MailSenderConfig;
import pl.lodz.it.sitodruk.service.EmailSenderService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.MimeMessageHelper;
import pl.lodz.it.sitodruk.utils.MessageProvider;

import java.util.Locale;
import java.util.ResourceBundle;


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
    public void sendRegistrationEmail(String to , HttpServletRequest request,String token,String lanuage) {
        try {
            MimeMessage mimeMessage = emailSender.getJavaMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String link = request.getRequestURL()
                    .substring(0, (request.getRequestURL().length() - request.getServletPath().length())).concat("/confirmAccount?token=");
            String body = "<a href=\"" + link + token + "\">"+ MessageProvider.getTranslatedText("confirm.account.msg",lanuage) + "</a>";
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(MessageProvider.getTranslatedText("confirm.account.subject",lanuage));
            emailSender.getJavaMailSender().send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPasswordChangeEmail(String to , HttpServletRequest request,String token,String lanuage) {
        try {



            MimeMessage mimeMessage = emailSender.getJavaMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String link = request.getRequestURL()
                    .substring(0, (request.getRequestURL().length() - request.getServletPath().length())).concat("/setNewPassword?token=");
            String body = "<a href=\"" + link + token + "\">"+ MessageProvider.getTranslatedText("reset.password.msg", lanuage) + "</a>";
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(MessageProvider.getTranslatedText("reset.password.subject", lanuage) );
            emailSender.getJavaMailSender().send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}