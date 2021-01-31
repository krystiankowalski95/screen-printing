package pl.lodz.it.sitodruk.exceptions;

public class InsufficientStockException extends BaseException{
    private final static String msg = "insufficient.stock";

    public InsufficientStockException(){
        super(msg);
    }
}
