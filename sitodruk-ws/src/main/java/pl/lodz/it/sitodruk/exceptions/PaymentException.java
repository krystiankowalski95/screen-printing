package pl.lodz.it.sitodruk.exceptions;

public class PaymentException extends BaseException{
    private final static String msg = "order.created";


    public PaymentException(){
        super(msg);
    }
}
