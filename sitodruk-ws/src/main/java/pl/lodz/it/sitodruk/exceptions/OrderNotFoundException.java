package pl.lodz.it.sitodruk.exceptions;

public class OrderNotFoundException extends BaseException{

    private final static String message = "order.not.found";

    public OrderNotFoundException(){
        super(message);
    }
}
