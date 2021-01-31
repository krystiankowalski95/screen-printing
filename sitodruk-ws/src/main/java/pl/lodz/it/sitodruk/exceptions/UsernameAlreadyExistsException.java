package pl.lodz.it.sitodruk.exceptions;

public class UsernameAlreadyExistsException extends BaseException{
    private final static String msg = "username.already.exists";

    public UsernameAlreadyExistsException(){
        super(msg);
    }
}
