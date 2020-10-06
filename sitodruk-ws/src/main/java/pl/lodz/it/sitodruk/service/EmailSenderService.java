package pl.lodz.it.sitodruk.service;

public interface EmailSenderService {
    void sendSimpleMessage(String to, String subject, String text);
}
