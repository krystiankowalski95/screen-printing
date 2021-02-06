package pl.lodz.it.sitodruk.exceptions;

public class UserNotActiveException extends BaseException{
    private final static String msg = "user.not.active";

    public UserNotActiveException(){
        super(msg);
    }
}
