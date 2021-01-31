package pl.lodz.it.sitodruk.exceptions;

public class EmailAlreadyExistsException extends BaseException{
    private final static String msg = "email.already.exists";

    public EmailAlreadyExistsException(){
        super(msg);
    }
}
