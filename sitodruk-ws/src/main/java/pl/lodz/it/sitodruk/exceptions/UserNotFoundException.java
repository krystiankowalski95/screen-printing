package pl.lodz.it.sitodruk.exceptions;

public class UserNotFoundException extends BaseException{

    private final static String msg = "user.not.found";
    public UserNotFoundException(){
        super(msg);
    }
}
