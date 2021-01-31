package pl.lodz.it.sitodruk.exceptions;

public class UserAccessLevelDeactivationException extends BaseException{
    private final static String msg = "cannot.deactivate.access.level";


    public UserAccessLevelDeactivationException(){
        super(msg);
    }
}
