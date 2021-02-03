package pl.lodz.it.sitodruk.exceptions;

public class EmailSendingException extends BaseException{

    private static final String message = "email.sending.exception";


    public EmailSendingException(){
        super(message);
    }
}
