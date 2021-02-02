package pl.lodz.it.sitodruk.service;

import javax.servlet.http.HttpServletRequest;

public interface EmailSenderService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendRegistrationEmail(String to, HttpServletRequest link, String token,String language);
    void sendPasswordChangeEmail(String to , HttpServletRequest request,String token,String language);
}
