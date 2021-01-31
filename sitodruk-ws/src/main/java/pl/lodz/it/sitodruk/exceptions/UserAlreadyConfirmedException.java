package pl.lodz.it.sitodruk.exceptions;

public class UserAlreadyConfirmedException extends BaseException{
    public final static String msg = "user.already.confirmed";

    public UserAlreadyConfirmedException(){
        super(msg);
    }
}
