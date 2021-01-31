package pl.lodz.it.sitodruk.exceptions;

public class InvalidOrderStatusException extends BaseException{
    private final static String msg = "invalid.order.status";

    public InvalidOrderStatusException(){
        super(msg);
    }
}
