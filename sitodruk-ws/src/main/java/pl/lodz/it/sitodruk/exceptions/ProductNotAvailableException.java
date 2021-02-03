package pl.lodz.it.sitodruk.exceptions;

public class ProductNotAvailableException extends BaseException{
    private final static String msg = "product.not.available";

    public ProductNotAvailableException(){
        super(msg);
    }
}
