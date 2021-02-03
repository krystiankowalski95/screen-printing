package pl.lodz.it.sitodruk.exceptions;

public class PaymentException extends BaseException{
    private final static String msg = "payment.exception";


    public PaymentException(){
        super(msg);
    }
}
