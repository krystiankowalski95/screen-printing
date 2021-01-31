package pl.lodz.it.sitodruk.exceptions;


public class BaseException extends Exception{
    private final static String message = "unexpected.error";

    public BaseException(){
        super(message);
    }

    public BaseException(String msg){
        super(msg);
    }
}
