package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.exceptions.EmailSendingException;

import javax.servlet.http.HttpServletRequest;

public interface EmailSenderService {
    void sendRegistrationEmail(String to, HttpServletRequest link, String token,String language) throws EmailSendingException;
    void sendPasswordChangeEmail(String to , HttpServletRequest request,String token,String language) throws EmailSendingException;
    void sendAccountBlockedEmail(String to ,String token,String lanuage) throws EmailSendingException;
}
